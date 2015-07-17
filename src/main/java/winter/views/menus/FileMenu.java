package winter.views.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import winter.Application;
import winter.Resources;
import winter.controllers.EditorsControllerImpl;
import winter.Settings;

import java.io.File;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class FileMenu extends Menu {
    private FileChooser openFileChooser;
    private FileChooser saveFileChooser;
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    
    public FileMenu() {
        super("File");
        init();
    }
    
    private void init() {
        MenuItem newFileItem = new MenuItem("New File", Resources.getIcon("new.png"));
        MenuItem openFileItem = new MenuItem("Open File...", Resources.getIcon("open.png"));
        MenuItem openFolderItem = new MenuItem("Open Folder...");
        MenuItem saveFileItem = new MenuItem("Save", Resources.getIcon("save.png"));
        MenuItem saveAsFileItem = new MenuItem("Save As...", Resources.getIcon("save_as.png"));
        MenuItem exitFileItem = new MenuItem("Exit"); 

        openFileItem.setOnAction(e -> EditorsControllerImpl.openFile());
        openFolderItem.setOnAction(e -> openFolder());
        newFileItem.setOnAction(e -> newFile());
        saveFileItem.setOnAction(e -> EditorsControllerImpl.saveFile(EditorsControllerImpl.getActiveEditor()));
        saveAsFileItem.setOnAction(e -> EditorsControllerImpl.saveAsFile(EditorsControllerImpl.getActiveEditor()));
        exitFileItem.setOnAction(e -> Application.exit());
        
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
        Optional.ofNullable(directoryChooser.showDialog(Application.getMainStage())).ifPresent(file -> {
            Application.PROJECTS_VIEW.displayProject(file.toPath());
            directoryChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    private void newFile() {
        Application.EDITORS_VIEW.newUntitledTab();
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
        return Optional.ofNullable(openFileChooser.showOpenDialog(Application.getMainStage()));
    }
    
    public Optional<File> showSaveDialog() {
        return Optional.ofNullable(saveFileChooser.showSaveDialog(Application.getMainStage()));
    }
}
