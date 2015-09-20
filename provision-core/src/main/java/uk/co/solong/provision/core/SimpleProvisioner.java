package uk.co.solong.provision.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.solong.linode4j.Linode;
import uk.co.solong.linode4j.jobmanager.JobManager;
import uk.co.solong.linode4j.mappings.DataCenterMapper;
import uk.co.solong.linode4j.mappings.DiskMapper;
import uk.co.solong.linode4j.mappings.DistributionMapper;
import uk.co.solong.linode4j.mappings.KernelMapper;
import uk.co.solong.linode4j.mappings.LinodeConfigMapper;
import uk.co.solong.linode4j.mappings.LinodeMapper;
import uk.co.solong.linode4j.mappings.PlanMapper;
import uk.co.solong.linode4j.mappings.StackScriptMapper;
import uk.co.solong.linode4j.mappings.UnknownMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by terabyte on 23/12/2014.
 */
public class SimpleProvisioner implements Provisioner {
    private Linode linode;
    private JobManager jobManager;
    private PasswordGenerator passwordGenerator = new PasswordGenerator();
    private static final Logger logger = LoggerFactory.getLogger(SimpleProvisioner.class);

    public SimpleProvisioner(Linode linode) {
        this.linode = linode; 
        jobManager = new JobManager(linode);
    }
 
    @Override
    public void buildFirstTime(LinodeConfig config) throws IOException, LinodeExists, InterruptedException {
        // in order to create a linode, we need a data center (derived from a
        // region) and a plan).
        // we now derive that information from the config

        // read the location that you want to deploy to (e.g. london)

        // get the list of available datacenters, and check what the id is based
        // on the region
        logger.info("Finding datacenter");
        int dataCenterId = extractDataCenterId(config);
        logger.info("Finding plan");
        int planId = extractPlanId(config);
        logger.info("Creating linode");
        int linodeId = extractLinodeId(dataCenterId, planId, config);

        // rename the linode
        logger.info("Renaming linode");
        JsonNode result = linode.updateLinode(linodeId).withLabel(config.getName()).asJson();

        // need to create a stackscript, stackscripts are bound to a particular
        // OS, find distId from the OS in the config.
        logger.info("Finding OS");
        JsonNode distributions = linode.availableDistributions().asJson();
        DistributionMapper distMapper = new DistributionMapper(distributions);
        int distributionId = distMapper.getDistributionIdFromOs(config.getOs());

        logger.info("Delete existing stackscript");
        // delete any existing stackscripts
        JsonNode existingStackScripts = linode.listStackScripts().asJson();
        StackScriptMapper stackScriptMapper = new StackScriptMapper(existingStackScripts);
        Integer existingStackScriptId = stackScriptMapper.getStackScriptIdFromLabel(config.getStackScriptLabel());
        if (existingStackScriptId != null) {
            JsonNode deletedStackScriptResult = linode.deleteStackScript(existingStackScriptId).asJson();
        }

        logger.info("Creating stackscript");
        // actually create the stackscript
        JsonNode jsonNode = linode.createStackScript(config.getStackScriptLabel(), String.valueOf(distributionId), config.getStackScript())
                .withDescription(config.getStackScriptDescription()).withIsPublic(config.isStackScriptPublic())
                .withRevNote(config.getStackScriptRevisionNote()).asJson();
        int stackScriptId = jsonNode.get("DATA").get("StackScriptID").asInt();

        // now create a disk image using the stackscript by creating the primary
        // followed by any others as secondaries.
        List<Disk> disks = config.getDisks();
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        Collection<Callable<Void>> tasks = new ArrayList<Callable<Void>>(3);
        tasks.add(() -> {
            logger.info("Creating primary disk");
            createPrimaryDisk(config, linodeId, distributionId, stackScriptId, disks);
            return Void.TYPE.newInstance();
        });
        tasks.add(() -> {
            logger.info("Creating secondary disk");
            createSecondaryDisks(linodeId, disks);
            return Void.TYPE.newInstance();
        });
        tasks.add(() -> {
            logger.info("Creating swap disk");
            createSwapDisk(linodeId, disks);
            return Void.TYPE.newInstance();
        });
        executor.invokeAll(tasks);
       
       

        // linode.createc
        // wait for job to complete
        logger.info("Finding kernel");
        JsonNode availableKernels = linode.availableKernels().asJson();
        KernelMapper kernelMapper = new KernelMapper(availableKernels);

        Integer kernelId = null;
        if (config.isLatestKernel()) {
            if (config.isBit64Kernel()) {
                kernelId = kernelMapper.findLatest(true);
            } else {
                kernelId = kernelMapper.findLatest(false);
            }
        } else {
            kernelMapper.getKernelIdFromLabel(config.getKernel());
        }

        logger.info("Creating config");
        JsonNode createConfig = linode.createLinodeConfig(linodeId, kernelId, config.getName() + "_config", Disk.generateDiskString(disks)).asJson();
        logger.info("Booting linode");
        JsonNode bootResult = linode.bootLinode(linodeId).asJson();

        // int configId = linode.createConfig();
        // linode.boot(linodeId,configId);
        int jobId = bootResult.get("DATA").get("JobID").asInt();
        JsonNode jobStatus = jobManager.waitForJob(linodeId, jobId);
        logger.info("Done");
    }

    private void createSwapDisk(int linodeId, List<Disk> disks) {
        // TODO Auto-generated method stub
        Disk swapDisk = Disk.findSwap(disks);
        JsonNode createDiskResult = linode.createLinodeDisk(linodeId, swapDisk.getLabel(), swapDisk.getType(), swapDisk.getSize()).asJson();
        int jobId = createDiskResult.get("DATA").get("JobID").asInt();
        JsonNode jobStatus = jobManager.waitForJob(linodeId, jobId);
        swapDisk.setDiskId(createDiskResult.get("DATA").get("DiskID").asInt());
    }

    private void createSecondaryDisks(int linodeId, List<Disk> disks) {
        List<Disk> secondaryDisks = Disk.findSecondary(disks);
        for (Disk disk : secondaryDisks) {
            JsonNode secondaryDiskResult = linode.createLinodeDisk(linodeId, disk.getLabel(), disk.getType(), disk.getSize()).asJson();
            ObjectMapper m = new ObjectMapper();
            try {
                logger.info("Result:{}",m.writeValueAsString(secondaryDiskResult));
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            int jobId = secondaryDiskResult.get("DATA").get("JobID").asInt(-1);
            if (jobId != -1) {
            JsonNode jobStatus = jobManager.waitForJob(linodeId, jobId);
            disk.setDiskId(secondaryDiskResult.get("DATA").get("DiskID").asInt());
            } else {
               
                try {
                    logger.error("And error occured while creating a secondary disk {}",m.writeValueAsString(secondaryDiskResult));
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }
    }

    private void createPrimaryDisk(LinodeConfig config, int linodeId, int distributionId, int stackScriptId, List<Disk> disks) {
        Disk primary = Disk.findPrimary(disks);
        ObjectMapper m = new ObjectMapper();
        Map<String, String> responses = config.getStackScriptResponses();
        JsonNode udfResponses = m.valueToTree(responses);
        logger.info("UDF Responses {}", udfResponses);
        
        String password = config.getRootPassword();
        if (config.isGenerateUnknownPassword()){
            password = passwordGenerator.nextSessionId();
        }
        JsonNode createDiskResult = linode.createLinodeDiskFromStackScript(linodeId, stackScriptId, udfResponses, distributionId, primary.getLabel(),
                primary.getSize(), password)
        // .withRootSSHKey(config.getRootSshKey())
                .asJson();

        // wait for job to complete
        int jobId = createDiskResult.get("DATA").get("JobID").asInt();
        JsonNode jobStatus = jobManager.waitForJob(linodeId, jobId);
        primary.setDiskId(createDiskResult.get("DATA").get("DiskID").asInt());
        // at some point maybe offer some kind of 'future object' where any
        // linode call can be wrapped by a function (belongong to a
        // futureFactory class) that takes
        // a jsonnode and returns a future. calling get on the future will block
        // waiting.
    }

    private int extractLinodeId(int dataCenterId, int planId, LinodeConfig config) throws LinodeExists {
        // actually create the linode
        // check linode doesn't already exist
        JsonNode linodes = linode.listLinode().asJson();
        LinodeMapper mapper = new LinodeMapper(linodes);
        try {
            int linodeId = mapper.getLinodeIdFromLabel(config.getName());
        } catch (UnknownMapping e) {
            JsonNode node = linode.createLinode(dataCenterId, planId).withPaymentTerm(1).asJson();
            int linodeId = node.get("DATA").get("LinodeID").asInt();
            return linodeId;
        }
        throw new LinodeExists();

    }

    private int extractPlanId(LinodeConfig config) {
        JsonNode plans = linode.availableLinodePlans().asJson();
        PlanMapper m = new PlanMapper(plans);
        int planId = m.getPlanIdFromRam(config.getRam());
        return planId;
    }

    private int extractDataCenterId(LinodeConfig config) {
        JsonNode node = linode.availableDataCenters().asJson();
        DataCenterMapper dcm = new DataCenterMapper(node);
        int dataCenterId = dcm.getDataCenterIdFromAbbr(config.getRegion());
        return dataCenterId;
    }

    @Override
    public void rebuildLoseData(LinodeConfig config) throws IOException, LinodeExists, InterruptedException {
        destroy(config);

        buildFirstTime(config);
    }

    private void discoverSecondaryDisk(LinodeConfig config, DiskMapper diskMapper) {
        // TODO Auto-generated method stub
        List<Disk> configDisks = config.getDisks();
        List<Disk> secondaryDisks = Disk.findSecondary(configDisks);
        for (Disk currentDisk : secondaryDisks) {
            int diskId = diskMapper.getDiskIdFromLabel(currentDisk.getLabel());
            currentDisk.setDiskId(diskId);
        }
    }

    private void discoverSwapDisk(LinodeConfig config, DiskMapper diskMapper) {
        // TODO Auto-generated method stub
        List<Disk> disks = config.getDisks();
        Disk swap = Disk.findSwap(disks);
        int diskId = diskMapper.getDiskIdFromLabel(swap.getLabel());
        swap.setDiskId(diskId);

    }

    @Override
    public void rebuildKeepData(LinodeConfig config) {
        // find the linodeId by config label
        logger.info("Rebuilding linode (keep data)");
        logger.info("Finding linode");
        JsonNode linodeList = linode.listLinode().asJson();
        LinodeMapper linodeMapper = new LinodeMapper(linodeList);
        int linodeId = linodeMapper.getLinodeIdFromLabel(config.getName());
        JsonNode shutDownResult = linode.shutdownLinode(linodeId).asJson();
        logger.info("Shutting down linode");

        int shutDownjobId = shutDownResult.get("DATA").get("JobID").asInt();
        JsonNode jobStatus = jobManager.waitForJob(linodeId, shutDownjobId);

        // find the primary disk in config
        Disk primary = Disk.findPrimary(config.getDisks());
        // get the label of this primary disk
        String label = primary.getLabel();
        // get the list of all disks on the linode
        JsonNode linodeDisks = linode.listLinodeDisk(linodeId).asJson();
        // find the diskId of the disk with the label found above.
        DiskMapper diskMapper = new DiskMapper(linodeDisks);
        try {
            logger.info("Deleting disk {}", label);
            int diskId = diskMapper.getDiskIdFromLabel(label);
            JsonNode deleteLinodeDiskResult = linode.deleteLinodeDisk(linodeId, diskId).asJson();

            int jobId = deleteLinodeDiskResult.get("DATA").get("JobID").asInt();
            JsonNode deleteJObStatus = jobManager.waitForJob(linodeId, jobId);
        } catch (UnknownMapping e) {
            logger.warn("Primary disk was already deleted or was not found: {}", label);
        }
        // same as before:
        // need to create a stackscript, stackscripts are bound to a particular
        // OS, find distId from the OS in the config.
        logger.info("Finding distro");
        JsonNode distributions = linode.availableDistributions().asJson();
        DistributionMapper distMapper = new DistributionMapper(distributions);
        int distributionId = distMapper.getDistributionIdFromOs(config.getOs());

        // delete any existing stackscripts
        JsonNode existingStackScripts = linode.listStackScripts().asJson();
        StackScriptMapper stackScriptMapper = new StackScriptMapper(existingStackScripts);
        Integer existingStackScriptId = stackScriptMapper.getStackScriptIdFromLabel(config.getStackScriptLabel());
        if (existingStackScriptId != null) {
            logger.info("Deleting existing stackscript");
            JsonNode deletedStackScriptResult = linode.deleteStackScript(existingStackScriptId).asJson();
        }

        logger.info("Creating stackscript");
        // actually create the stackscript
        JsonNode jsonNode = linode.createStackScript(config.getStackScriptLabel(), "" + distributionId, config.getStackScript())
                .withDescription(config.getStackScriptDescription()).withIsPublic(config.isStackScriptPublic())
                .withRevNote(config.getStackScriptRevisionNote()).asJson();
        int stackScriptId = jsonNode.get("DATA").get("StackScriptID").asInt();

        // now create a disk image using the stackscript by creating the primary
        // followed by any others as secondaries.
        List<Disk> disks = config.getDisks();
        logger.info("Creating primary");
        createPrimaryDisk(config, linodeId, distributionId, stackScriptId, disks);
        logger.info("Finding Secondary");
        discoverSecondaryDisk(config, diskMapper);
        logger.info("Finding swap");
        discoverSwapDisk(config, diskMapper);

        // linode.createc
        // wait for job to complete
        logger.info("Finding kernels");
        JsonNode availableKernels = linode.availableKernels().withIsXen(true).asJson();
        KernelMapper kernelMapper = new KernelMapper(availableKernels);

        Integer kernelId = null;
        if (config.isLatestKernel()) {
            if (config.isBit64Kernel()) {
                kernelId = kernelMapper.findLatest(true);
            } else {
                kernelId = kernelMapper.findLatest(false);
            }
        } else {
            kernelMapper.getKernelIdFromLabel(config.getKernel());
        }

        logger.info("Finding config");
        JsonNode linodeConfigs = linode.listLinodeConfigs(linodeId).asJson();
        LinodeConfigMapper linodeConfigMapper = new LinodeConfigMapper(linodeConfigs);
        int configId = linodeConfigMapper.getConfigIdFromLinodeId(config.getName() + "_config");
        // delete the config
        logger.info("Deleting config");
        JsonNode deleteLinodeConfig = linode.deleteLinodeConfig(linodeId, configId).asJson();
        logger.info("Creating config");
        JsonNode createConfig = linode.createLinodeConfig(linodeId, kernelId, config.getName() + "_config", Disk.generateDiskString(disks)).asJson();
        logger.info("Booting linode");
        JsonNode bootResult = linode.bootLinode(linodeId).asJson();
        int jobId = bootResult.get("DATA").get("JobID").asInt();
        JsonNode bootJobStatus = jobManager.waitForJob(linodeId, jobId);
        logger.info("Done");
    }

    @Override
    public void destroy(LinodeConfig config) {
        // find the linodeId by config label
        logger.info("Destroying and rebuilding linode (destroy data)");
        logger.info("Finding linode");
        JsonNode linodeList = linode.listLinode().asJson();
        LinodeMapper linodeMapper = new LinodeMapper(linodeList);
        int linodeId = linodeMapper.getLinodeIdFromLabel(config.getName());
        logger.info("Shutting down linode");
        JsonNode shutDownResult = linode.shutdownLinode(linodeId).asJson();
        int jobId = shutDownResult.get("DATA").get("JobID").asInt();
        JsonNode n = jobManager.waitForJob(linodeId, jobId);

        JsonNode linodeDisks = linode.listLinodeDisk(linodeId).asJson();
        // find the diskId of the disk with the label found above.
        DiskMapper diskMapper = new DiskMapper(linodeDisks);
        List<Disk> disks = config.getDisks();
        
        disks.parallelStream().forEach( (disk) -> {
            try {
                logger.info("Deleting disk {} ", disk.getLabel());
                int diskId = diskMapper.getDiskIdFromLabel(disk.getLabel());
                JsonNode deleteDiskResult = linode.deleteLinodeDisk(linodeId, diskId).asJson();
                int deleteDiskJobId = deleteDiskResult.get("DATA").get("JobID").asInt();
                JsonNode waitForJobResult = jobManager.waitForJob(linodeId, deleteDiskJobId);
                logger.info("Disk deleted");
            } catch (UnknownMapping m) {
                logger.warn("Disk doesn't exist or is already deleted {}", disk.getLabel());
            }
        });
        logger.info("Deleting linode");
        JsonNode deleteLinodeResult = linode.deleteLinode(linodeId).asJson();
        logger.info("Done");
    }

}
