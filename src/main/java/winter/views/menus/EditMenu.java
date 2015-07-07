package winter.views.menus;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import winter.controllers.EditorController;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class EditMenu extends Menu {
    public EditMenu() {
        super("Edit");
        init();
    }
    
    private void init() {
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        MenuItem find = new MenuItem("Find...");
        MenuItem replace = new MenuItem("Replace...");
        MenuItem copy = new MenuItem("Copy");
        MenuItem cut = new MenuItem("Cut");
        MenuItem paste = new MenuItem("Paste");
        
        undo.disableProperty().bind(
                ((BooleanBinding) EditorController.getActiveCodeArea().undoAvailableProperty()).not());
        redo.disableProperty().bind(
                ((BooleanBinding) EditorController.getActiveCodeArea().redoAvailableProperty()).not());
        
        undo.setOnAction(e -> EditorController.undo());
        redo.setOnAction(e -> EditorController.redo());
        copy.setOnAction(e -> EditorController.copy());
        cut.setOnAction(e -> EditorController.cut());
        paste.setOnAction(e -> EditorController.paste());
        
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
}
