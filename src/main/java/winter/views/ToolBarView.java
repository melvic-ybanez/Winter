package winter.views;

import javafx.geometry.Side;
import javafx.scene.control.*;
import winter.factories.Icons;
import winter.utils.StreamUtils;
import winter.views.menus.*;

import java.util.List;

/**
 * Created by ybamelcash on 7/8/2015.
 */
public class ToolBarView extends ToolBar {
    private Button newButton = Icons.createIconedButton(Icons.createNewFileIcon());
    private Button openFileButton = Icons.createIconedButton(Icons.createOpenFileIcon());
    private Button openFolderButton = Icons.createIconedButton(Icons.createClosedDirectoryIcon());
    private Button saveButton = Icons.createIconedButton(Icons.createSaveIcon());
    private Button saveAsButton = Icons.createIconedButton(Icons.createSaveAsIcon());
    private Button restartButton = Icons.createIconedButton(Icons.createRestartIcon());
    private Button undoButton = Icons.createIconedButton(Icons.createUndoIcon());
    private Button redoButton = Icons.createIconedButton(Icons.createRedoIcon());
    private Button copyButton = Icons.createIconedButton(Icons.createCopyIcon());
    private Button cutButton = Icons.createIconedButton(Icons.createCutIcon());
    private Button pasteButton = Icons.createIconedButton(Icons.createPasteIcon());
    private Button findButton = Icons.createIconedButton(Icons.createFindIcon());
    private Button findFileButton = Icons.createIconedButton(Icons.createFindFileIcon());
    private Button replaceButton = Icons.createIconedButton(Icons.createReplaceIcon());
    private Button viewProjectsButton = Icons.createIconedButton(Icons.createProjectsIcon());
    private Button preferencesButton = Icons.createIconedButton(Icons.createPreferencesIcon());
    private Button helpButton = Icons.createIconedButton(Icons.createHelpIcon());
    
    private FileMenu fileMenu;
    private EditMenu editMenu;
    private ViewMenu viewMenu;
    private NavigationMenu navigationMenu;
    private PreferencesMenu preferencesMenu;
    
    public ToolBarView(FileMenu fileMenu, EditMenu editMenu, NavigationMenu navigationMenu, PreferencesMenu preferencesMenu) {
        super();
        this.fileMenu = fileMenu;
        this.editMenu = editMenu;
        this.navigationMenu = navigationMenu;
        this.preferencesMenu = preferencesMenu;
    }
    
    public void createUI() {
        getStyleClass().add("meruem-toolbar");
        
        newButton.setTooltip(createTooltip(fileMenu.getNewFileItem()));
        openFileButton.setTooltip(createTooltip(fileMenu.getOpenFileItem()));
        openFolderButton.setTooltip(createTooltip(fileMenu.getOpenDirectoryItem()));
        saveButton.setTooltip(createTooltip(fileMenu.getSaveFileItem()));
        saveAsButton.setTooltip(createTooltip(fileMenu.getSaveAsFileItem()));
        restartButton.setTooltip(createTooltip(fileMenu.getRestartItem()));
        
        undoButton.setTooltip(createTooltip(editMenu.getUndoItem()));
        redoButton.setTooltip(createTooltip(editMenu.getRedoItem()));
        copyButton.setTooltip(createTooltip(editMenu.getCopyItem()));
        cutButton.setTooltip(createTooltip(editMenu.getCutItem()));
        pasteButton.setTooltip(createTooltip(editMenu.getPasteItem()));
        findButton.setTooltip(createTooltip(editMenu.getFindItem()));
        findFileButton.setTooltip(createTooltip(navigationMenu.getGoToFile()));
        replaceButton.setTooltip(createTooltip(editMenu.getReplaceItem()));
        viewProjectsButton.setTooltip(new Tooltip(viewMenu.getProjectsItem().getText()));

        configurePreferencesButton();

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
        String displayText = menuItem.getAccelerator() == null ? "": "(" + menuItem.getAccelerator().getDisplayText() + ")";
        return new Tooltip(menuItem.getText() + " " + displayText);
    }
    
    private void registerEvents() {
        newButton.setOnAction(fileMenu.getNewFileItem().getOnAction());
        openFileButton.setOnAction(fileMenu.getOpenFileItem().getOnAction());
        openFolderButton.setOnAction(fileMenu.getOpenDirectoryItem().getOnAction());
        saveButton.setOnAction(fileMenu.getSaveFileItem().getOnAction());
        saveAsButton.setOnAction(fileMenu.getSaveAsFileItem().getOnAction());
        restartButton.setOnAction(fileMenu.getRestartItem().getOnAction());
        
        undoButton.setOnAction(editMenu.getUndoItem().getOnAction());
        redoButton.setOnAction(editMenu.getRedoItem().getOnAction());
        copyButton.setOnAction(editMenu.getCopyItem().getOnAction());
        cutButton.setOnAction(editMenu.getCutItem().getOnAction());
        pasteButton.setOnAction(editMenu.getPasteItem().getOnAction());
        findButton.setOnAction(editMenu.getFindItem().getOnAction());
        findFileButton.setOnAction(navigationMenu.getGoToFile().getOnAction());
        replaceButton.setOnAction(editMenu.getReplaceItem().getOnAction());
        
        viewProjectsButton.setOnAction(event -> 
                viewMenu.getProjectsItem().setSelected(!viewMenu.getProjectsItem().isSelected()));
        
        undoButton.disableProperty().bind(editMenu.getUndoItem().disableProperty());
        redoButton.disableProperty().bind(editMenu.getRedoItem().disableProperty());
    }

    private void configurePreferencesButton() {
        preferencesButton.setTooltip(new Tooltip("Preferences"));
        preferencesButton.setOnAction(e -> preferencesMenu.getGeneralPrefController().showUI());
    }

    public void setViewMenu(ViewMenu viewMenu) {
        this.viewMenu = viewMenu;
    }
}
