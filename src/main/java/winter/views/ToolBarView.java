package winter.views;

import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import winter.factories.Icons;
import winter.views.menus.EditMenu;
import winter.views.menus.FileMenu;

/**
 * Created by ybamelcash on 7/8/2015.
 */
public class ToolBarView extends ToolBar {
    private Button newButton = Icons.createButtonIcon("new.png");
    private Button openButton = Icons.createButtonIcon("open.png");
    private Button saveButton = Icons.createButtonIcon("save.png");
    private Button saveAsButton = Icons.createButtonIcon("save_as.png");
    private Button undoButton = Icons.createButtonIcon("undo.png");
    private Button redoButton = Icons.getRedoButton();
    private Button copyButton = Icons.createButtonIcon("copy.png");
    private Button cutButton = Icons.createButtonIcon("cut.png");
    private Button pasteButton = Icons.createButtonIcon("paste.png");
    private Button findButton = Icons.createButtonIcon("find.png");
    private Button replaceButton = Icons.createButtonIcon("replace.png");
    private Button runButton = Icons.createButtonIcon("run.png");
    private Button replButton = Icons.createButtonIcon("repl.png");
    private Button stopButton = Icons.createButtonIcon("stop.png");
    private Button preferencesButton = Icons.createButtonIcon("preferences.png");
    private Button helpButton = Icons.createButtonIcon("help.png");
    
    private FileMenu fileMenu;
    private EditMenu editMenu;
    
    public ToolBarView(FileMenu fileMenu, EditMenu editMenu) {
        super();
        this.fileMenu = fileMenu;
        this.editMenu = editMenu;
        initUI();
        registerEvents();
    }
    
    private void initUI() {
        getStyleClass().add("meruem-toolbar");
        
        newButton.setTooltip(createTooltip(fileMenu.getNewFileItem()));
        openButton.setTooltip(createTooltip(fileMenu.getOpenFileItem()));
        saveButton.setTooltip(createTooltip(fileMenu.getSaveFileItem()));
        saveAsButton.setTooltip(createTooltip(fileMenu.getSaveAsFileItem()));
        undoButton.setTooltip(createTooltip(editMenu.getUndoItem()));
        redoButton.setTooltip(createTooltip(editMenu.getRedoItem()));
        copyButton.setTooltip(createTooltip(editMenu.getCopyItem()));
        cutButton.setTooltip(createTooltip(editMenu.getCutItem()));
        pasteButton.setTooltip(createTooltip(editMenu.getPasteItem()));
        findButton.setTooltip(createTooltip(editMenu.getFindItem()));
        replaceButton.setTooltip(createTooltip(editMenu.getReplaceItem()));

        getItems().addAll(newButton, openButton, saveButton, saveAsButton, new Separator(),
                undoButton, redoButton, new Separator(),
                copyButton, cutButton, pasteButton, new Separator(),
                findButton, replaceButton, new Separator(),
                preferencesButton, helpButton);
        
        managedProperty().bind(visibleProperty());
    }
    
    private Tooltip createTooltip(MenuItem menuItem) {
        return new Tooltip(menuItem.getText() + " (" + menuItem.getAccelerator().getDisplayText() + ")");
    }
    
    private void registerEvents() {
        newButton.setOnAction(fileMenu.getNewFileItem().getOnAction());
        openButton.setOnAction(fileMenu.getOpenFileItem().getOnAction());
        saveButton.setOnAction(fileMenu.getSaveFileItem().getOnAction());
        saveAsButton.setOnAction(fileMenu.getSaveAsFileItem().getOnAction());
        undoButton.setOnAction(editMenu.getUndoItem().getOnAction());
        redoButton.setOnAction(editMenu.getRedoItem().getOnAction());
        copyButton.setOnAction(editMenu.getCopyItem().getOnAction());
        cutButton.setOnAction(editMenu.getCutItem().getOnAction());
        pasteButton.setOnAction(editMenu.getPasteItem().getOnAction());
        findButton.setOnAction(editMenu.getFindItem().getOnAction());
        replaceButton.setOnAction(editMenu.getReplaceItem().getOnAction());
        
        undoButton.disableProperty().bind(editMenu.getUndoItem().disableProperty());
        redoButton.disableProperty().bind(editMenu.getRedoItem().disableProperty());
    }
}
