import uk.co.solong.linode.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by terabyte on 23/12/2014.
 */
public class SimpleProvisioner implements Provisioner{
    private LinodeTemplate linode;
    SimpleProvisioner(){
        linode = new LinodeTemplate("uffd3swMzjHN9CYh5lZHt4rtS17N2JtBlQrxfXgWUxpBrJkRRL9Yu7c3fD8G4fHe");
    }
    @Override
    public void buildFirstTime(Config config) throws IOException {
        List<DataCenter> dataCenters = linode.getAvailableDataCenters();
        List<Plan> plans = linode.getAvailablePlans();
        DataCenterMapper dcm = new DataCenterMapper(dataCenters);
        int dataCenterId = dcm.getDataCenterIdFromAbbr(config.getRegion());
        PlanMapper m = new PlanMapper(plans);
        int planId = m.getPlanIdFromRam(config.getRam());
        int linodeId = linode.createLinode(dataCenterId,planId,1);;
        linode.updateLabel(linodeId,config.getName());
        List<Distribution> distributions = linode.getAvailableDistributions();
        DistributionMapper distMapper = new DistributionMapper(distributions);
        int distributionId = distMapper.getDistributionIdFromLabel(config.getOs());

        //int stackScriptId = linode.createStackScript(label,description,distributionId,isPublic,rev_note,script);

        //CreateDiskResponse cdrOs = linode.createFromStackScript(linodeId,stackScriptId,null,distributionId,ubuntu);
        //wait for job to complete
        //CreateDiskResponse cdrData = linode.createDisk(linodeID);
        //wait for job to complete
        //int configId = linode.createConfig();
        //linode.boot(linodeId,configId);

    }

    @Override
    public void rebuildDataLoss(Config config) {

    }

    @Override
    public void rebuildDataSafe(Config config) {

    }
}
