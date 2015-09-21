package uk.co.solong.provision.core;
import java.io.IOException;

/**
 * Created by terabyte on 23/12/2014.
 */
public interface Provisioner {
    public void buildFirstTime(LinodeConfiguration config) throws IOException, LinodeExists, InterruptedException;
    public void rebuildLoseData(LinodeConfiguration config) throws IOException, LinodeExists, InterruptedException;
    public void rebuildKeepData(LinodeConfiguration config);
    public void destroy(LinodeConfiguration config);
}
