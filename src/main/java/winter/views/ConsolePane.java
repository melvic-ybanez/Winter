package winter.views;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.TitledPane;
import winter.Application;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ConsolePane extends TitledPane {
    public ConsolePane(ReadOnlyDoubleProperty heightProperty) {
        setText("Console");
        prefHeightProperty().bind(heightProperty);
        setCollapsible(false);
    }
}
