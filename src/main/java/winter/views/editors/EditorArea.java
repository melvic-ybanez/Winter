package winter.views.editors;

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
public class EditorArea extends CodeArea implements Observer {
    EditorModel editorModel;
    EditorController editorController;
    
    public EditorArea(EditorController editorController, EditorModel editorModel) {
        setEditorModel(editorModel);
        setEditorController(editorController);
        editorModel.registerObserver(this);
        
        getStyleClass().add("meruem-codearea");
        
        /* These are hardcoded values for now */
        setStyle("-fx-font-family: Consolas");

        setParagraphGraphicFactory(LineNumberFactory.get(this));
        textProperty().addListener((obs, oldText, newText) -> {
            EditorsControllerImpl.editorAreaChanged(this, newText);
        });
        caretPositionProperty().addListener((obs, oldPos, newPos) -> {
            EditorsControllerImpl.editorAreaChanged(this, getText());
        });

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (EditorsControllerImpl.runAccelerators(event))
                return;

            switch (event.getCode()) {
                case TAB:
                    insertText(getCaretPosition(), Settings.TAB_STRING);
                    event.consume();
                    break;
                case ENTER:
                    String autoIndentedNewLineString = EditorsControllerImpl.getAutoIndentedNewLineString();
                    insertText(getCaretPosition(), autoIndentedNewLineString);
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

    @Override
    public void update() {
        
    }
}
