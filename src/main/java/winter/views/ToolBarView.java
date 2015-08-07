package winter.views;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import winter.factories.Icons;
import winter.views.menus.EditMenu;
import winter.views.menus.FileMenu;
import winter.views.menus.ViewMenu;

/**
 * Created by ybamelcash on 7/8/2015.
 */
public class ToolBarView extends ToolBar {
    private Button newButton = Icons.createButtonIcon("new.png");
    private Button openFileButton = Icons.createButtonIcon("open_file.png");
    private Button openFolderButton = Icons.createButtonIcon("close_folder.png");
    private Button saveButton = Icons.createButtonIcon("save.png");
    private Button saveAsButton = Icons.createButtonIcon("save_as.png");
    private Button restartButton = Icons.createButtonIcon("restart.png");
    private Button undoButton = Icons.createButtonIcon("undo.png");
    private Button redoButton = Icons.createButtonIcon("redo.png");
    private Button copyButton = Icons.createButtonIcon("copy.png");
    private Button cutButton = Icons.createButtonIcon("cut.png");
    private Button pasteButton = Icons.createButtonIcon("paste.png");
    private Button findButton = Icons.createButtonIcon("find.png");
    private Button findFileButton = Icons.createButtonIcon("find_file.png");
    private Button replaceButton = Icons.createButtonIcon("replace.png");
    private Button runButton = Icons.createButtonIcon("run.png");
    private Button replButton = Icons.createButtonIcon("repl.png");
    private Button stopButton = Icons.createButtonIcon("stop.png");
    private Button viewProjectsButton = Icons.createButtonIcon("view_projects.png");
    private Button preferencesButton = Icons.createButtonIcon("preferences.png");
    private Button helpButton = Icons.createButtonIcon("help.png");
    
    private FileMenu fileMenu;
    private EditMenu editMenu;
    private ViewMenu viewMenu;
    
    public ToolBarView(FileMenu fileMenu, EditMenu editMenu) {
        super();
        this.fileMenu = fileMenu;
        this.editMenu = editMenu;
    }
    
    public void createUI() {
        getStyleClass().add("meruem-toolbar");
        
        newButton.setTooltip(createTooltip(fileMenu.getNewFileItem()));
        openFileButton.setTooltip(createTooltip(fileMenu.getOpenFileItem()));
        openFolderButton.setTooltip(createTooltip(fileMenu.getOpenFolderItem()));
        saveButton.setTooltip(createTooltip(fileMenu.getSaveFileItem()));
        saveAsButton.setTooltip(createTooltip(fileMenu.getSaveAsFileItem()));
        
        undoButton.setTooltip(createTooltip(editMenu.getUndoItem()));
        redoButton.setTooltip(createTooltip(editMenu.getRedoItem()));
        copyButton.setTooltip(createTooltip(editMenu.getCopyItem()));
        cutButton.setTooltip(createTooltip(editMenu.getCutItem()));
        pasteButton.setTooltip(createTooltip(editMenu.getPasteItem()));
        findButton.setTooltip(createTooltip(editMenu.getFindItem()));
        replaceButton.setTooltip(createTooltip(editMenu.getReplaceItem()));
        viewProjectsButton.setTooltip(new Tooltip(viewMenu.getProjectsItem().getText()));

        getItems().addAll(newButton, 
                openFileButton, openFolderButton, saveButton, saveAsButton, restartButton, new Separator(),
                undoButton, redoButton, new Separator(),
                copyButton, cutButton, pasteButton, new Separator(),
                findButton, replaceButton, findFileButton, new Separator(),
                viewProjectsButton, preferencesButton, helpButton);
        
        managedProperty().bind(visibleProperty());
        
        registerEvents();
    }
    
    private Tooltip createTooltip(MenuItem menuItem) {
        return new Tooltip(menuItem.getText() + " (" + menuItem.getAccelerator().getDisplayText() + ")");
    }
    
    private void registerEvents() {
        newButton.setOnAction(fileMenu.getNewFileItem().getOnAction());
        openFileButton.setOnAction(fileMenu.getOpenFileItem().getOnAction());
        openFolderButton.setOnAction(fileMenu.getOpenFolderItem().getOnAction());
        saveButton.setOnAction(fileMenu.getSaveFileItem().getOnAction());
        saveAsButton.setOnAction(fileMenu.getSaveAsFileItem().getOnAction());
        
        undoButton.setOnAction(editMenu.getUndoItem().getOnAction());
        redoButton.setOnAction(editMenu.getRedoItem().getOnAction());
        copyButton.setOnAction(editMenu.getCopyItem().getOnAction());
        cutButton.setOnAction(editMenu.getCutItem().getOnAction());
        pasteButton.setOnAction(editMenu.getPasteItem().getOnAction());
        findButton.setOnAction(editMenu.getFindItem().getOnAction());
        replaceButton.setOnAction(editMenu.getReplaceItem().getOnAction());
        
        viewProjectsButton.setOnAction(event -> 
                viewMenu.getProjectsItem().setSelected(!viewMenu.getProjectsItem().isSelected()));
        
        undoButton.disableProperty().bind(editMenu.getUndoItem().disableProperty());
        redoButton.disableProperty().bind(editMenu.getRedoItem().disableProperty());
    }

    public void setViewMenu(ViewMenu viewMenu) {
        this.viewMenu = viewMenu;
    }
}
