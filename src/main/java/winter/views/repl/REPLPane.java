package winter.views.repl;

import javafx.scene.control.TitledPane;
import winter.Application;

/**
 * Created by ybamelcash on 7/4/2015.
 */
public class REPLPane extends TitledPane {
    public REPLPane() {
        setText("REPL");
        prefHeightProperty().bind(Application.bottomSplitPane.heightProperty());
        setCollapsible(false);
    }
}
