package winter.views;

import javafx.scene.control.TitledPane;
import winter.Globals;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ConsolePane extends TitledPane {
    public ConsolePane() {
        setText("Console");
        prefHeightProperty().bind(Globals.bottomSplitPane.heightProperty());
        setCollapsible(false);
    }
}
