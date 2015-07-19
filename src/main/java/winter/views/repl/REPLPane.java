package winter.views.repl;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.TitledPane;

/**
 * Created by ybamelcash on 7/4/2015.
 */
public class REPLPane extends TitledPane {
    public REPLPane(ReadOnlyDoubleProperty heightProperty) {
        setText("REPL");
        prefHeightProperty().bind(heightProperty);
        setCollapsible(false);
    }
}
