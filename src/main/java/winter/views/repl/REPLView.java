package winter.views.repl;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.TitledPane;
import org.fxmisc.richtext.CodeArea;
import winter.controllers.repls.REPLController;
import winter.models.repls.REPLModel;

/**
 * Created by ybamelcash on 7/4/2015.
 */
public class REPLView extends TitledPane {
    private REPLController replController;
    private REPLModel replModel;
    private CodeArea codeArea;
    
    public REPLView(REPLController replController, REPLModel replModel, ReadOnlyDoubleProperty heightProperty) {
        this.replController = replController;
        this.replModel = replModel;
        prefHeightProperty().bind(heightProperty);
        init();
    }
    
    public void init() {
        setText("REPL");
        setCollapsible(false);
        
        codeArea = new CodeArea();
        setContent(codeArea);
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }
}
