package winter.views;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import winter.Globals;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ConsolePane extends TitledPane {
    public ConsolePane() {
        setText("Console");
        prefHeightProperty().bind(Globals.bottomPane.heightProperty());
        setCollapsible(false);
    }
}
