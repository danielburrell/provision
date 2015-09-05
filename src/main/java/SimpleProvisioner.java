import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import uk.co.solong.linode4j.Linode;
import uk.co.solong.linode4j.mappings.DataCenterMapper;
import uk.co.solong.linode4j.mappings.PlanMapper;

/**
 * Created by terabyte on 23/12/2014.
 */
public class SimpleProvisioner implements Provisioner {
    private Linode linode;

    SimpleProvisioner() {
        linode = new Linode("uffd3swMzjHN9CYh5lZHt4rtS17N2JtBlQrxfXgWUxpBrJkRRL9Yu7c3fD8G4fHe");
    }

    @Override
    public void buildFirstTime(Config config) throws IOException {
        //in order to create a linode, we need a data center (derived from a region) and a plan).
        //we now derive that information from the config
        
        //read the location that you want to deploy to (e.g. london)
        
        //get the list of available datacenters, and check what the id is based on the region
        
        int dataCenterId = extractDataCenterId(config);
        JsonNode plans = linode.availableLinodePlans().asJson();
        PlanMapper m = new PlanMapper(plans);
        int planId = m.getPlanIdFromRam(config.getRam());
        

        //actually create the linode
        JsonNode node = linode.createLinode(dataCenterId,planId).withPaymentTerm(1).asJson();
        
        int linodeId = node.get("DATA").get("LinodeID").asInt();
        
        //rename the linode
        JsonNode result = linode.updateLinode(linodeId).withLabel(config.getName()).asJson();
        
        //need to create a stackscript, stackscripts are bound to a particular OS, find distId from the OS in the config.
        List<Distribution> distributions = linode.getAvailableDistributions();
        DistributionMapper distMapper = new DistributionMapper(distributions);
        int distributionId = distMapper.getDistributionIdFromLabel(config.getOs());
        //actually create the stackscript
        int stackScriptId = linode.createStackScript(config.getStackScriptLabel(),config.getStackScriptDescription(),Arrays.asList(distributionId),config.isStackScriptPublic(),config.getStackScriptRevisionNote(),config.getStackScript());

        //now create a disk image using the stackscript.
        List<Disk> disks = config.getDisks();
        Disk primary = Disk.findPrimary(disks);
        CreateDiskResponse cdrOs = linode.createDiskFromStackScript(linodeId, stackScriptId, null, distributionId, primary.getLabel(), primary.getSize(), config.getRootPassword(), config.getRootSshKey());//(linodeId,stackScriptId,null,distributionId,disk., config., pass, sshKey);
        //wait for job to complete
        cdrOs.
        //CreateDiskResponse cdrData = linode.createDisk(linodeID);
        //wait for job to complete
        //int configId = linode.createConfig();
        //linode.boot(linodeId,configId);

    }

    private int extractDataCenterId(Config config) {
        JsonNode node = linode.availableDataCenters().asJson();
        DataCenterMapper dcm = new DataCenterMapper(node);
        int dataCenterId = dcm.getDataCenterIdFromAbbr(config.getRegion());
        return dataCenterId;
    }

    @Override
    public void rebuildDataLoss(Config config) {

    }

    @Override
    public void rebuildDataSafe(Config config) {

    }
}
