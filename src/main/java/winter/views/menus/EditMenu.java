package winter.views.menus;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import winter.Globals;
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
        
        undo.disableProperty().bind(
                ((BooleanBinding) EditorController.getActiveCodeArea().undoAvailableProperty()).not());
        redo.disableProperty().bind(
                ((BooleanBinding) EditorController.getActiveCodeArea().redoAvailableProperty()).not());
        
        undo.setOnAction(e -> EditorController.undo());
        redo.setOnAction(e -> EditorController.redo());
        
        undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN));
        redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCodeCombination.CONTROL_DOWN));
        
        getItems().addAll(undo, redo);
    }
}
