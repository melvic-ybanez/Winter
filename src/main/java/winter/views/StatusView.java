package winter.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.StatusBar;
import winter.controllers.editors.EditorSetController;
import winter.models.editors.EditorModel;
import winter.models.statuses.StatusModel;
import winter.utils.Observable;
import winter.utils.Observer;

/**
 * Created by ybamelcash on 8/1/2015.
 */
public class StatusView extends StatusBar implements Observer {
    private Label lineLabel = new Label("");
    private Label columnLabel = new Label("");

    private StatusModel statusModel;
    
    public StatusView(StatusModel statusModel) {
        setStatusModel(statusModel); 
        statusModel.registerObserver(this);
        init();
    }
    
    private void init() {
        setText("");
        HBox rightPane = new HBox();
        rightPane.getChildren().addAll(new Separator(Orientation.VERTICAL), lineLabel, columnLabel);
        rightPane.setSpacing(5);
        rightPane.setPadding(new Insets(0, 15, 0, 15));
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        getRightItems().add(rightPane);
    }

    public StatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(StatusModel statusModel) {
        this.statusModel = statusModel;
    }

    @Override
    public void update() {
        lineLabel.setText("Line: " + (statusModel.getLineNumber() + 1));
        columnLabel.setText("Column: " + (statusModel.getColumnNumber() + 1));
        if (statusModel.areChangesSaved()) {
            setText("Changes saved.");
        }
    }
    
    private EditorSetController getEditorSetController() {
        return statusModel.getEditorSetController();
    }
}
