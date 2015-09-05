import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by terabyte on 23/12/2014.
 */
public class TestApplication {
    @Test
    public void test() throws IOException {
        SimpleProvisioner s = new SimpleProvisioner();
        Config config = new Config();
        config.setRam(1024);
        config.setRegion("london");
        config.setName("heavy");
        List<Disk> disks = new ArrayList<Disk>();
        Disk e = new Disk();
        e.setSize(18000);
        e.setLabel("os");
        e.setPrimary(true);
        e.setReadOnly(false);
        e.setType("ext4");
        disks.add(e);
        config.setDisks(disks);
        s.buildFirstTime(config);
        
    }

}
