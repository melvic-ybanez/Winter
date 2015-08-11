package winter.utils;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
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
        } catch (NoSuchFileException ex) {
            IOException ex1 = new NoSuchFileException(ex.getFile(), ex.getOtherFile(), "File does not exists.");
            return Optional.of(ex1);
        } catch (IOException e) {
            return Optional.of(e);
        }
    }
}
