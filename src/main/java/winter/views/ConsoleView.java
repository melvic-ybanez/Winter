package winter.views;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.TitledPane;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ConsoleView extends TitledPane {
    public ConsoleView(ReadOnlyDoubleProperty heightProperty) {
        setText("Console");
        prefHeightProperty().bind(heightProperty);
        setCollapsible(false);
    }
}
