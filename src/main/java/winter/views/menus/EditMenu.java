package winter.views.menus;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import winter.Resources;
import winter.controllers.EditorController;
import winter.controllers.EditorSetController;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class EditMenu extends Menu {
    private EditorSetController editorSetController;
    
    public EditMenu(EditorSetController editorSetController) {
        super("Edit");
        setEditorSetController(editorSetController);
        init();
    }
    
    private void init() {
        MenuItem undo = new MenuItem("Undo", Resources.getIcon("undo.png"));
        MenuItem redo = new MenuItem("Redo", Resources.getRedoIcon());
        MenuItem find = new MenuItem("Find...", Resources.getIcon("find.png"));
        MenuItem replace = new MenuItem("Replace...", Resources.getIcon("replace.png"));
        MenuItem copy = new MenuItem("Copy", Resources.getIcon("copy.png"));
        MenuItem cut = new MenuItem("Cut", Resources.getIcon("cut.png"));
        MenuItem paste = new MenuItem("Paste", Resources.getIcon("paste.png"));
        
        EditorController editorController = editorSetController.getActiveEditorController();
        
        undo.disableProperty().bind(
                ((BooleanBinding) editorController.getEditorView().undoAvailableProperty()).not());
        redo.disableProperty().bind(
                ((BooleanBinding) editorController.getEditorView().redoAvailableProperty()).not());
        
        undo.setOnAction(e -> editorController.undo());
        redo.setOnAction(e -> editorController.redo());
        copy.setOnAction(e -> editorController.copy());
        cut.setOnAction(e -> editorController.cut());
        paste.setOnAction(e -> editorController.paste());
        
        undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN));
        redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCodeCombination.CONTROL_DOWN));
        find.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN));
        replace.setAccelerator(new KeyCodeCombination(KeyCode.F, 
                KeyCodeCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN));
        copy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN));
        cut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCodeCombination.CONTROL_DOWN));
        paste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCodeCombination.CONTROL_DOWN));
        
        getItems().addAll(undo, redo, new SeparatorMenuItem(), 
                find, replace, new SeparatorMenuItem(),
                copy, cut, paste);
    }

    public EditorSetController getEditorSetController() {
        return editorSetController;
    }

    public void setEditorSetController(EditorSetController editorSetController) {
        this.editorSetController = editorSetController;
    }
}
