package winter.models.repls;

import java.util.Optional;

/**
 * Created by ybamelcash on 7/25/2015.
 */
public interface REPLModel {
    public Optional<Exception> run();
    
    public void stop();
}
