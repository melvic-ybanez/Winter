package winter.views.menus;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import winter.Resources;
import winter.controllers.editors.EditorSetController;
import winter.factories.Icons;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class EditMenu extends Menu {
    private EditorSetController editorSetController;
    private MenuItem undoItem = new MenuItem("Undo", Resources.getIcon("undo.png"));
    private MenuItem redoItem = new MenuItem("Redo", Icons.getRedoImageView());
    private MenuItem findItem = new MenuItem("Find...", Resources.getIcon("find.png"));
    private MenuItem replaceItem = new MenuItem("Replace...", Resources.getIcon("replace.png"));
    private MenuItem copyItem = new MenuItem("Copy", Resources.getIcon("copy.png"));
    private MenuItem cutItem = new MenuItem("Cut", Resources.getIcon("cut.png"));
    private MenuItem pasteItem = new MenuItem("Paste", Resources.getIcon("paste.png"));
    
    public EditMenu(EditorSetController editorSetController) {
        super("Edit"); 
        setEditorSetController(editorSetController);
        
        editorSetController.getEditorSetView().getEditorContextMenu().getItems().addAll(createMenuItems());
        getItems().addAll(createMenuItems());
    }
    
    private List<MenuItem> createMenuItems() {
        undoItem = new MenuItem("Undo", Resources.getIcon("undo.png"));
        redoItem = new MenuItem("Redo", Resources.getIcon("redo.png"));
        findItem = new MenuItem("Find...", Resources.getIcon("find.png"));
        replaceItem = new MenuItem("Replace...", Resources.getIcon("replace.png"));
        copyItem = new MenuItem("Copy", Resources.getIcon("copy.png"));
        cutItem = new MenuItem("Cut", Resources.getIcon("cut.png"));
        pasteItem = new MenuItem("Paste", Resources.getIcon("paste.png"));
        
        undoItem.disableProperty().bind(
                ((BooleanBinding) editorSetController.getActiveEditorView().undoAvailableProperty()).not());
        redoItem.disableProperty().bind(
                ((BooleanBinding) editorSetController.getActiveEditorView().redoAvailableProperty()).not());
        
        undoItem.setOnAction(e -> editorSetController.getActiveEditorController().undo());
        redoItem.setOnAction(e -> editorSetController.getActiveEditorController().redo());
        findItem.setOnAction(e -> editorSetController.getActiveEditorView().getFindView().showUI());
        replaceItem.setOnAction(e -> editorSetController.getActiveEditorView().getReplaceView().showUI());
        copyItem.setOnAction(e -> editorSetController.getActiveEditorController().copy());
        cutItem.setOnAction(e -> editorSetController.getActiveEditorController().cut());
        pasteItem.setOnAction(e -> editorSetController.getActiveEditorController().paste());
        
        undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN));
        redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCodeCombination.CONTROL_DOWN));
        findItem.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN));
        replaceItem.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCodeCombination.CONTROL_DOWN));
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN));
        cutItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCodeCombination.CONTROL_DOWN));
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCodeCombination.CONTROL_DOWN));
        
        return Arrays.asList(undoItem, redoItem, new SeparatorMenuItem(),
                findItem, replaceItem, new SeparatorMenuItem(),
                copyItem, cutItem, pasteItem);
    }

    public EditorSetController getEditorSetController() {
        return editorSetController;
    }

    public void setEditorSetController(EditorSetController editorSetController) {
        this.editorSetController = editorSetController;
    }

    public MenuItem getUndoItem() {
        return undoItem;
    }

    public MenuItem getRedoItem() {
        return redoItem;
    }

    public MenuItem getFindItem() {
        return findItem;
    }

    public MenuItem getReplaceItem() {
        return replaceItem;
    }

    public MenuItem getCopyItem() {
        return copyItem;
    }

    public MenuItem getCutItem() {
        return cutItem;
    }

    public MenuItem getPasteItem() {
        return pasteItem;
    }
}
