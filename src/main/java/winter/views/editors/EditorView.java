package winter.views.editors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import winter.Settings;
import winter.controllers.EditorController;
import winter.controllers.EditorsControllerImpl;
import winter.models.EditorModel;
import winter.utils.Observer;

/**
 * Created by ybamelcash on 7/16/2015.
 */
public class EditorView extends CodeArea implements Observer {
    private EditorModel editorModel;
    private EditorController editorController;
    private Property<Node> graphicProperty = new SimpleObjectProperty<>(new Label());
    
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
            if (editorController.runAccelerators(event))
                return;

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
}
