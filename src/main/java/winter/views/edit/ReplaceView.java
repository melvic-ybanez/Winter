package winter.views.edit;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import winter.utils.Observer;

/**
 * Created by ybamelcash on 7/21/2015.
 */
public class ReplaceView extends VBox implements Observer {
    private FindView findView;
    private TextField replaceField;
    private Button replaceButton;
    private Button replaceAllButton;
    private BorderPane replacePane;
    
    public ReplaceView(FindView findView) {
        setFindView(findView);
        replacePane = new BorderPane();
        getChildren().addAll(findView, replacePane);
        getStyleClass().addAll("find-replace-pane");
        visibleProperty().bind(Bindings.when(
                replacePane.visibleProperty().not().and(findView.visibleProperty().not()))
                .then(false).otherwise(true));
    } 
    
    public void showUI() {
        if (replaceField == null) {
            replaceField = new TextField();
            replaceField.setPromptText("Enter the new string"); 
            //replaceField.prefColumnCountProperty().bindBidirectional(findView.getFindField().prefColumnCountProperty());
            
            replaceButton = new Button("Replace");
            replaceAllButton = new Button("Replace All");

            HBox leftPane = new HBox();
            leftPane.getChildren().addAll(replaceField, replaceButton, replaceAllButton);
            leftPane.setStyle("-fx-spacing: 10");
            
            replacePane.setLeft(leftPane);

            setSpacing(5);
            replacePane.setPadding(new Insets(0, 0, 3, 3));
            
            registerEvents();
        }
        
        findView.showUI();
        findView.getFindField().requestFocus();
        replacePane.setVisible(true);
    }
    
    private void registerEvents() {
        
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
}
