package winter;

import javafx.scene.control.TitledPane;
import winter.Application;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ConsolePane extends TitledPane {
    public ConsolePane() {
        setText("Console");
        prefHeightProperty().bind(Application.bottomSplitPane.heightProperty());
        setCollapsible(false);
    }
}
