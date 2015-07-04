package winter.views.repl;

import javafx.scene.control.TitledPane;
import winter.Globals;

/**
 * Created by ybamelcash on 7/4/2015.
 */
public class REPLPane extends TitledPane {
    public REPLPane() {
        setText("REPL");
        prefHeightProperty().bind(Globals.bottomPane.heightProperty());
        setCollapsible(false);
    }
}
