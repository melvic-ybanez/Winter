package winter.views.editor;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import winter.controllers.editors.EditorController;
import winter.controllers.editors.EditorSetController;
import winter.models.editors.EditorModel;
import winter.utils.Observer;
import winter.views.RequiredTextInputDialog;
import winter.views.edit.FindView;
import winter.views.edit.ReplaceView;

import java.util.Optional;

/**
 * Created by ybamelcash on 7/16/2015.
 */
public class EditorView extends CodeArea implements Observer {
    private EditorModel editorModel;
    private EditorController editorController;
    private Property<Node> graphicProperty = new SimpleObjectProperty<>(new Label());
    private ReplaceView replaceView;
    
    public EditorView(EditorController editorController, EditorModel editorModel) {
        setEditorModel(editorModel); 
        setEditorController(editorController); 
        editorModel.registerObserver(this);
        
        getStyleClass().add("meruem-codearea");

        EditorSetController editorSetController = editorController.getEditorSetController();

        styleProperty().bind(editorSetController.getEditorSetView().fontStyleStringProperty());
        wrapTextProperty().bind(editorSetController.getGeneralPrefController().getGeneralPrefModel().wrapTextProperty());

        setParagraphGraphicFactory(new LineNumberView(this)); 
        textProperty().addListener((obs, oldText, newText) -> {
            editorController.editorAreaChanged(newText);
        });
        caretPositionProperty().addListener((obs, oldPos, newPos) -> {
            editorController.editorAreaChanged(getText()); 
        });

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (editorController.runAccelerators(event)) {
                event.consume();
                return;
            }

            switch (event.getCode()) {
                case TAB:
                    insertText(getCaretPosition(), editorController
                            .getEditorModel().getGeneralPrefModel().getTabString());
                    event.consume();
                    break;
                case ENTER:
                    editorController.autoIndent();
                    event.consume();
                    break;
            }
        });
    }
    
    public Optional<String> showRenameDialog() {
        RequiredTextInputDialog renameDialog = new RequiredTextInputDialog(editorModel.getTitle());
        renameDialog.setTitle("Rename File");
        renameDialog.setContentText("Enter the new filename");
        return renameDialog.getAnswer();
    }
    
    public Optional<ButtonType> showUnsavedDialog() {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setHeaderText(null);
        saveAlert.setContentText("Do you want to save the changes made to " + editorModel.getTitle() + "?");
        saveAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        return saveAlert.showAndWait();
    }

    public EditorModel getEditorModel() {
        return editorModel;
    }

    public void setEditorModel(EditorModel editorModel) {
        this.editorModel = editorModel;
        this.editorModel.contentsProperty().bind(textProperty());
        this.editorModel.caretPositionProperty().bind(caretPositionProperty());
        this.editorModel.lineNumberProperty().bind(currentParagraphProperty());
        this.editorModel.columnNumberProperty().bind(caretColumnProperty());
    }

    public EditorController getEditorController() {
        return editorController;
    }

    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }
    
    public Property<Node> graphicProperty() {
        return graphicProperty;
    }

    @Override
    public void update() {
        getEditorController().updateTabGraphic();
    }

    public FindView getFindView() {
        return replaceView.getFindView();
    }

    public ReplaceView getReplaceView() {
        return replaceView; 
    }

    public void setReplaceView(ReplaceView replaceView) {
        this.replaceView = replaceView;
    }
}
