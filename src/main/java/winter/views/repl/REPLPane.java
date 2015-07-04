package winter.views.repl;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import winter.Globals;

/**
 * Created by ybamelcash on 7/4/2015.
 */
public class REPLPane extends TitledPane {
    public REPLPane() {
        setText("REPL");
        prefHeightProperty().bind(Globals.bottomSplitPane.heightProperty());
        setCollapsible(false);
        setOpacity(0.83);
    }
}
