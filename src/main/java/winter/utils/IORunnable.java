package winter.utils;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by ybamelcash on 8/9/2015.
 */
public interface IORunnable {
    public void run() throws IOException;

    public static Optional<IOException> run(IORunnable runnable) {
        try {
            runnable.run();
            return Optional.empty();
        } catch (IOException e) {
            return Optional.of(e);
        }
    }
}
