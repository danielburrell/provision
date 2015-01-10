import org.junit.Test;

import java.io.IOException;

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
        s.buildFirstTime(config);
    }

}
