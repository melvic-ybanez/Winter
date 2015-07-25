package winter.models.repls;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by ybamelcash on 7/25/2015.
 */
public class REPLModelImpl implements REPLModel {
    private Process process;
    private ProcessBuilder processBuilder;
    
    @Override
    public Optional<Exception> run() {
        try {
            process = getProcessBuilder().start();
        } catch (IOException e) {
            return Optional.of(e);
        }
        
        return Optional.empty();
    }

    @Override
    public void stop() {
        process.destroy();
    }
    
    public ProcessBuilder getProcessBuilder() {
        if (processBuilder == null) {
            processBuilder = new ProcessBuilder("C:/Program Files/Java/jdk1.8.0_45/jre/bin",
                    "-jar", "d://winter-sample/meruem.jar");
        }
        
        return processBuilder;
    }
}
