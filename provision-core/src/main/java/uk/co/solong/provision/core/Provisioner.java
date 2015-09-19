package uk.co.solong.provision.core;
import java.io.IOException;

/**
 * Created by terabyte on 23/12/2014.
 */
public interface Provisioner {
    public void buildFirstTime(LinodeConfig config) throws IOException, LinodeExists;
    public void rebuildLoseData(LinodeConfig config) throws IOException, LinodeExists;
    public void rebuildKeepData(LinodeConfig config);
}