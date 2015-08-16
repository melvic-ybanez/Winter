package winter.views.menus;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import winter.Settings;
import winter.controllers.editors.EditorSetController;
import winter.controllers.files.FileController;
import winter.controllers.projects.ProjectSetController;
import winter.factories.Icons;

import java.io.File;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class FileMenu extends Menu {
    private FileChooser openFileChooser;
    private FileChooser saveFileChooser;
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    private EditorSetController editorSetController;
    private ProjectSetController projectSetController;
    private FileController fileController;
    private Stage stage;
    
    private MenuItem newFileItem;
    private MenuItem openFileItem;
    private MenuItem openFolderItem;
    private MenuItem saveFileItem;
    private MenuItem saveAsFileItem;
    private MenuItem exitFileItem;
    
    public FileMenu(FileController fileController, EditorSetController editorSetController, ProjectSetController projectSetController) {
        super("File");
        setEditorSetController(editorSetController);
        setProjectSetController(projectSetController);
        setFileController(fileController);
        init();
    }
    
    private void init() {
        newFileItem = new MenuItem("New File", Icons.createNewFileIcon());
        openFileItem = new MenuItem("Open File...", Icons.createOpenFileIcon());
        openFolderItem = new MenuItem("Open Directory...", Icons.createClosedDirectoryIcon());
        saveFileItem = new MenuItem("Save", Icons.createSaveIcon());
        saveAsFileItem = new MenuItem("Save As...", Icons.createSaveAsIcon());
        exitFileItem = new MenuItem("Exit"); 

        openFileItem.setOnAction(e -> fileController.openFile());
        openFolderItem.setOnAction(e -> openFolder());
        newFileItem.setOnAction(e -> newFile());
        saveFileItem.setOnAction(e -> fileController.saveFile());
        saveAsFileItem.setOnAction(e -> fileController.saveAsFile());
        exitFileItem.setOnAction(e -> {
            if (editorSetController.closeAllTabs()) {
                Platform.exit();
            }
        });
        
        newFileItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN));
        openFileItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openFolderItem.setAccelerator(new KeyCodeCombination(KeyCode.O, 
                KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        saveFileItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAsFileItem.setAccelerator(new KeyCodeCombination(KeyCode.S, 
                KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        exitFileItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        
        getItems().addAll(newFileItem, 
                openFileItem, openFolderItem, new SeparatorMenuItem(),
                saveFileItem, saveAsFileItem, new SeparatorMenuItem(),
                exitFileItem);
    }
    
    private void openFolder() {
        directoryChooser.setTitle("Open Folder"); 
        Optional.ofNullable(directoryChooser.showDialog(getStage())).ifPresent(file -> {
            projectSetController.openProject(file.toPath());
            directoryChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    private void newFile() {
        editorSetController.getEditorSetView().newUntitledTab();
    }
    
    public FileChooser getOpenFileChooser() {
        if (openFileChooser == null) {
            openFileChooser = new FileChooser();
            openFileChooser.setTitle("Open File");
            Settings.SUPPORTED_FILE_FORMATS.forEach(format -> {
                openFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                        format.getString("description"),
                        "*" + format.getString("extension")));
            });
        }
        return openFileChooser;
    }
    
    public FileChooser getSaveFileChooser() {
        if (saveFileChooser == null) {
            saveFileChooser = new FileChooser();
            saveFileChooser.setTitle("Save As");
        }
        return saveFileChooser;
    }
    
    public Optional<File> showOpenDialog() {
        return Optional.ofNullable(openFileChooser.showOpenDialog(getStage()));
    }
    
    public Optional<File> showSaveDialog() {
        return Optional.ofNullable(saveFileChooser.showSaveDialog(getStage()));
    }
    
    public Stage getStage() {
        if (stage == null) {
            stage = (Stage) editorSetController.getEditorSetView().getScene().getWindow();
        } 
        return stage;
    }

    public EditorSetController getEditorSetController() {
        return editorSetController;
    }

    public void setEditorSetController(EditorSetController editorSetController) {
        this.editorSetController = editorSetController;
    }

    public ProjectSetController getProjectSetController() {
        return projectSetController;
    }

    public void setProjectSetController(ProjectSetController projectSetController) {
        this.projectSetController = projectSetController;
    }

    public FileController getFileController() {
        return fileController;
    }

    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }

    public MenuItem getNewFileItem() {
        return newFileItem;
    }

    public MenuItem getOpenFileItem() {
        return openFileItem;
    }

    public MenuItem getSaveFileItem() {
        return saveFileItem;
    }

    public MenuItem getSaveAsFileItem() {
        return saveAsFileItem;
    }

    public MenuItem getOpenFolderItem() {
        return openFolderItem;
    }
}
