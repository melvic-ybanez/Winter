package winter.controllers.repls;

import javafx.beans.property.ReadOnlyDoubleProperty;
import winter.models.repls.REPLModel;
import winter.views.repl.REPLView;

/**
 * Created by ybamelcash on 7/26/2015.
 */
public class REPLControllerImpl implements REPLController {
    private REPLModel replModel;
    private REPLView replView;
    
    public REPLControllerImpl(REPLModel replModel, ReadOnlyDoubleProperty heightProperty) {
        setREPLModel(replModel);
        setREPLView(new REPLView(this, replModel, heightProperty));
    }
    
    @Override
    public void run() {
        
    }

    @Override
    public void stop() {

    }

    public REPLModel getREPLModel() {
        return replModel;
    }

    public void setREPLModel(REPLModel replModel) {
        this.replModel = replModel;
    }

    public REPLView getREPLView() {
        return replView;
    }

    public void setREPLView(REPLView replView) {
        this.replView = replView;
    }
}
