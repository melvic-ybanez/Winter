package winter.views.edit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import winter.controllers.edits.FindController;
import winter.factories.Icons;
import winter.models.edits.FindModel;
import winter.utils.Observable;
import winter.utils.SimpleObservable;

/**
 * Created by ybamelcash on 7/8/2015.
 */
public class FindView extends BorderPane {
    private Observable observable;
    private FindController findController;
    private FindModel findModel;

    private TextField findField;
    private Button nextButton;
    private Button previousButton;
    private Button hideButton;
    
    public FindView(FindController findController, FindModel findModel) {
        setFindController(findController);
        setFindModel(findModel);
        observable = new SimpleObservable();
        
        getStyleClass().addAll("find-replace-pane", "meruem-toolbar");
    }
    
    public void showUI() {
        if (findField == null) {
            findField = new TextField();
            findField.setPromptText("Search String");

            HBox checkBoxPane = new HBox();
            CheckBox matchCaseCheck = new CheckBox("Match Case");
            CheckBox wordsCheck = new CheckBox("Words");
            checkBoxPane.getChildren().addAll(matchCaseCheck, wordsCheck);
            checkBoxPane.setAlignment(Pos.CENTER);
            checkBoxPane.setSpacing(7);

            findField.textProperty().bindBidirectional(findModel.queryStringProperty());
            matchCaseCheck.selectedProperty().bindBidirectional(findModel.matchCaseProperty());
            wordsCheck.selectedProperty().bindBidirectional(findModel.wordsProperty());

            HBox buttonsPane = new HBox();
            nextButton = Icons.createIconedButton(Icons.createDownIcon(), "Find next occurrence");
            previousButton = Icons.createIconedButton(Icons.createUpIcon(), "Find previous occurrence");
            buttonsPane.getChildren().addAll(nextButton, previousButton);
            buttonsPane.setAlignment(Pos.CENTER);
            buttonsPane.setSpacing(7);

            hideButton = Icons.createIconedButton(Icons.createHideIcon(), "Hide this panel");
            hideButton.setAlignment(Pos.CENTER_RIGHT);

            HBox leftPane = new HBox();
            leftPane.getChildren().addAll(findField, checkBoxPane, buttonsPane, hideButton);
            leftPane.setStyle("-fx-spacing: 10");

            setLeft(leftPane);
            setRight(hideButton);

            setPadding(new Insets(3, 5, 3, 3));
            
            registerEvents();
        } 
        
        findField.requestFocus();
        setVisible(true);
    }
    
    private void registerEvents() {
        nextButton.setOnAction(event -> findController.findNext());
        previousButton.setOnAction(event -> findController.findPrevious());
        findField.textProperty().addListener((obs, oldText, newText) -> {
            findController.getFindModel().resetPosition();
        });
        hideButton.setOnAction(event -> {
            setVisible(false);
            getObservable().notifyObservers();
        });
    }
    
    public void displayNoMatchDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("No Match Dialog");
        dialog.setHeaderText(null);
        dialog.setContentText("No match found");
        dialog.showAndWait();
    }

    public FindController getFindController() {
        return findController;
    }

    public void setFindController(FindController findController) {
        this.findController = findController;
    }

    public TextField getFindField() {
        return findField;
    }

    public void setFindField(TextField findField) {
        this.findField = findField;
    }

    public FindModel getFindModel() {
        return findModel;
    }

    public void setFindModel(FindModel findModel) {
        this.findModel = findModel;
    }

    public Observable getObservable() {
        return observable;
    }
}
