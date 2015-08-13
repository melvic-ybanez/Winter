package winter.utils;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by ybamelcash on 8/13/2015.
 */
public interface WatchableDir {
    public void register(Path dir) throws IOException;

    public void processEvents();

    public void start();
}
