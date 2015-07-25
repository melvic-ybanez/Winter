package winter.controllers.repls;

import winter.models.repls.REPLModel;
import winter.views.repl.REPLView;

/**
 * Created by ybamelcash on 7/26/2015.
 */
public interface REPLController {
    public void run();
    
    public void stop();
    
    public REPLModel getREPLModel();
    
    public REPLView getREPLView();
}
