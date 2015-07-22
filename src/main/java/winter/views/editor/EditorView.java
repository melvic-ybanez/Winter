package winter.views.editor;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import winter.Settings;
import winter.controllers.editors.EditorController;
import winter.models.editors.EditorModel;
import winter.utils.Observer;
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
        
        /* These are hardcoded values for now */
        setStyle("-fx-font-family: Consolas");

        setParagraphGraphicFactory(LineNumberFactory.get(this));
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
                    insertText(getCaretPosition(), Settings.TAB_STRING);
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
        TextInputDialog renameDialog = new TextInputDialog(editorModel.getTitle());
        renameDialog.setTitle("Rename File");
        renameDialog.setHeaderText(null);
        renameDialog.setContentText("Enter the new filename");
        return renameDialog.showAndWait();
    }
    
    public Optional<ButtonType> showUnsavedDialog() {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setHeaderText(null);
        saveAlert.setContentText("Do you want to save " + editorModel.getTitle() + "?");
        saveAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        return saveAlert.showAndWait();
    }

    public EditorModel getEditorModel() {
        return editorModel;
    }

    public void setEditorModel(EditorModel editorModel) {
        this.editorModel = editorModel;
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
        if (getEditorModel().unsaved()) {
            getEditorController().updateTabGraphic();
        }
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
