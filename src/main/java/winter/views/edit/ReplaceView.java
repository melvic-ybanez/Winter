package winter.views.edit;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import winter.controllers.edits.ReplaceController;
import winter.models.edits.ReplaceModel;
import winter.utils.Observer;

/**
 * Created by ybamelcash on 7/21/2015.
 */
public class ReplaceView extends VBox implements Observer {
    private ReplaceController replaceController;
    private ReplaceModel replaceModel;
    private FindView findView;
    private TextField replaceField;
    private Button replaceButton;
    private Button replaceAllButton;
    private HBox replacePane;
    
    public ReplaceView(ReplaceController replaceController, ReplaceModel replaceModel, FindView findView) {
        setReplaceController(replaceController);
        setReplaceModel(replaceModel);
        setFindView(findView);
        replacePane = new HBox();
        getChildren().addAll(findView, replacePane);
        getStyleClass().addAll("find-replace-pane");
        visibleProperty().bind(replacePane.visibleProperty().or(findView.visibleProperty()));
    } 
    
    public void showUI() {
        if (replaceField == null) {
            replaceField = new TextField();
            replaceField.setPromptText("Replacement String"); 
            replaceField.textProperty().bindBidirectional(replaceModel.replaceStringProperty());
            
            replaceButton = new Button("Replace");
            replaceAllButton = new Button("Replace All");

            HBox leftPane = new HBox();
            leftPane.getChildren().addAll(replaceField, replaceButton, replaceAllButton);
            leftPane.setStyle("-fx-spacing: 10");
            
            replacePane.getChildren().add(leftPane);

            setSpacing(5);
            replacePane.setPadding(new Insets(0, 0, 3, 3));
            
            registerEvents();
        }
        
        findView.showUI();
        findView.getFindField().requestFocus();
        replacePane.setVisible(true);
        replacePane.managedProperty().bind(replacePane.visibleProperty());
    }
    
    private void registerEvents() {
        replaceButton.setOnAction(e -> replaceController.replace());
        replaceAllButton.setOnAction(e -> replaceController.replaceAll());
    }

    public FindView getFindView() {
        return findView;
    }

    public void setFindView(FindView findView) {
        this.findView = findView;
        findView.getObservable().registerObserver(this);
    }

    @Override
    public void update() {
        if (!findView.isVisible()) {
            replacePane.setVisible(false);
        }
    }

    public ReplaceController getReplaceController() {
        return replaceController;
    }

    public void setReplaceController(ReplaceController replaceController) {
        this.replaceController = replaceController;
    }

    public ReplaceModel getReplaceModel() {
        return replaceModel;
    }

    public void setReplaceModel(ReplaceModel replaceModel) {
        this.replaceModel = replaceModel;
    }
}
