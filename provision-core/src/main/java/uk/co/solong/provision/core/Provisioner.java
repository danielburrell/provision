package uk.co.solong.provision.core;
import java.io.IOException;

/**
 * Created by terabyte on 23/12/2014.
 */
public interface Provisioner {
    public void buildFirstTime(LinodeConfig config) throws IOException, LinodeExists, InterruptedException;
    public void rebuildLoseData(LinodeConfig config) throws IOException, LinodeExists, InterruptedException;
    public void rebuildKeepData(LinodeConfig config);
    public void destroy(LinodeConfig config);
}
