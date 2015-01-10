import java.io.IOException;

/**
 * Created by terabyte on 23/12/2014.
 */
public interface Provisioner {
    public void buildFirstTime(Config config) throws IOException;
    public void rebuildDataLoss(Config config);
    public void rebuildDataSafe(Config config);
}
