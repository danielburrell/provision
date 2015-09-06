import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import uk.co.solong.linode4j.Linode;
import uk.co.solong.linode4j.mappings.DataCenterMapper;
import uk.co.solong.linode4j.mappings.DistributionMapper;
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
		// in order to create a linode, we need a data center (derived from a
		// region) and a plan).
		// we now derive that information from the config

		// read the location that you want to deploy to (e.g. london)

		// get the list of available datacenters, and check what the id is based
		// on the region

		int dataCenterId = extractDataCenterId(config);
		int planId = extractPlanId(config);
		int linodeId = extractLinodeId(dataCenterId, planId);

		// rename the linode
		JsonNode result = linode.updateLinode(linodeId).withLabel(config.getName()).asJson();

		// need to create a stackscript, stackscripts are bound to a particular
		// OS, find distId from the OS in the config.
		JsonNode distributions = linode.availableDistributions().asJson();
		DistributionMapper distMapper = new DistributionMapper(distributions);
		int distributionId = distMapper.getDistributionIdFromOs(config.getOs());

		// actually create the stackscript
		JsonNode jsonNode = linode
				.createStackScript(config.getStackScriptLabel(), ""+distributionId,
						URLEncoder.encode(config.getStackScript(), "UTF-8"))
				.withDescription(config.getStackScriptDescription()).withIsPublic(config.isStackScriptPublic())
				.withRevNote(config.getStackScriptRevisionNote()).asJson();
		int stackScriptId = jsonNode.get("DATA").get("StackScriptID").asInt();

		// now create a disk image using the stackscript by creating the primary
		// followed by any others as secondaries.
		List<Disk> disks = config.getDisks();
		createPrimaryDisks(config, linodeId, distributionId, stackScriptId, disks);
		createSecondaryDisks(linodeId, disks);

		// linode.createc
		// wait for job to complete
		// int configId = linode.createConfig();
		// linode.boot(linodeId,configId);

	}

	private void createSecondaryDisks(int linodeId, List<Disk> disks) {
		List<Disk> secondaryDisks = Disk.findSecondary(disks);
		for (Disk disk : secondaryDisks) {
			JsonNode secondaryDiskResult = linode
					.createLinodeDisk(linodeId, disk.getLabel(), disk.getType(), disk.getSize()).asJson();
		}
	}

	private void createPrimaryDisks(Config config, int linodeId, int distributionId, int stackScriptId,
			List<Disk> disks) {
		Disk primary = Disk.findPrimary(disks);
		JsonNode createDiskResult = linode.createLinodeDiskFromStackScript(linodeId, stackScriptId, null,
				distributionId, primary.getLabel(), primary.getSize(), config.getRootPassword())
				.withRootSSHKey(config.getRootSshKey()).asJson();
		// wait for job to complete
	}

	private int extractLinodeId(int dataCenterId, int planId) {
		// actually create the linode
		JsonNode node = linode.createLinode(dataCenterId, planId).withPaymentTerm(1).asJson();

		int linodeId = node.get("DATA").get("LinodeID").asInt();
		return linodeId;
	}

	private int extractPlanId(Config config) {
		JsonNode plans = linode.availableLinodePlans().asJson();
		PlanMapper m = new PlanMapper(plans);
		int planId = m.getPlanIdFromRam(config.getRam());
		return planId;
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
