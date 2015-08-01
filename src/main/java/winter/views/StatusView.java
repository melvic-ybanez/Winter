package winter.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import org.controlsfx.control.StatusBar;
import winter.models.statuses.StatusModel;

/**
 * Created by ybamelcash on 8/1/2015.
 */
public class StatusView extends StatusBar {
    private Label lineLabel = new Label("");
    private Label columnLabel = new Label("");

    private StatusModel statusModel;
    
    public StatusView(StatusModel statusModel) {
        setStatusModel(statusModel);
        init();
    }
    
    private void init() {
        setText("");
        
        getRightItems().addAll(lineLabel);
        
        lineLabel.textProperty().bind(new SimpleStringProperty("Line: " + statusModel.lineNumberProperty().get()));
    }

    public StatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(StatusModel statusModel) {
        this.statusModel = statusModel;
    }
}
