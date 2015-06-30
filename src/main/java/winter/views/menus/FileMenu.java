package winter.views.menus;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import winter.controllers.EditorController;
import winter.controllers.FileController;
import winter.utils.Either;
import winter.utils.Errors;
import winter.views.Settings;
import winter.Globals;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class FileMenu extends Menu {
    private FileChooser openFileChooser = new FileChooser();
    private FileChooser saveFileChooser = new FileChooser();
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    
    public FileMenu() {
        super("File");
        init();
    }
    
    private void init() {
        MenuItem newFileItem = new MenuItem("New File");
        MenuItem openFileItem = new MenuItem("Open File...");
        MenuItem openFolderItem = new MenuItem("Open Folder...");
        MenuItem saveFileItem = new MenuItem("Save");
        MenuItem saveAsFileItem = new MenuItem("Save As...");

        openFileItem.setOnAction(e -> openFile());
        openFolderItem.setOnAction(e -> openFolder());
        newFileItem.setOnAction(e -> newFile());
        saveFileItem.setOnAction(e -> saveFile());
        saveAsFileItem.setOnAction(e -> saveAsFile());
        
        newFileItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN));
        openFileItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openFolderItem.setAccelerator(new KeyCodeCombination(KeyCode.O, 
                KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        saveFileItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAsFileItem.setAccelerator(new KeyCodeCombination(KeyCode.S, 
                KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        
        getItems().addAll(newFileItem, openFileItem, openFolderItem, saveFileItem, saveAsFileItem);
    }
    
    public void openFile() {
        openFileChooser.setTitle("Open File"); 

        Settings.SUPPORTED_FILE_FORMATS.forEach(format -> {
            openFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    format.getString("description"),
                    "*" + format.getString("extension")));
        });

        Optional.ofNullable(openFileChooser.showOpenDialog(Globals.getMainStage())).ifPresent(file -> {
            Path path = file.toPath();
            Either<IOException, String> result = FileController.openFile(path);
            result.getLeft().ifPresent(Errors::openFileException);
            result.getRight().ifPresent(contents -> {
                Globals.editorPane.newEditorAreaTab(path, contents);
            });
            openFileChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    private void openFolder() {
        directoryChooser.setTitle("Open Folder"); 
        Optional.ofNullable(directoryChooser.showDialog(Globals.getMainStage())).ifPresent(file -> {
            Globals.projectsPane.displayProject(file.toPath());
            directoryChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    public void saveFile() {
        Either<IOException, Boolean> result = FileController.saveFile();
        result.getLeft().ifPresent(Errors::saveFileException);
        result.getRight().ifPresent(saved -> {
            if (!saved) {
                saveAsFile();
            };
        });
    }
    
    public void saveAsFile() {
        saveFileChooser.setTitle("Save As");
        Optional.ofNullable(saveFileChooser.showSaveDialog(Globals.getMainStage())).ifPresent(file -> {
            Path path = file.toPath();
            Either<IOException, Optional<String>> result = FileController.saveAsFile(path);

            result.getLeft().ifPresent(Errors::saveFileException);
            
            if (!result.getLeft().isPresent()) {
                result.getRight().ifPresent(errorOpt -> {
                    errorOpt.ifPresent(error -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Save File Error");
                        alert.setHeaderText("Unable to save file: " + path);
                        alert.setContentText(error);
                        alert.showAndWait();
                    });
                    
                    if (!errorOpt.isPresent()) {
                        saveFileChooser.setInitialDirectory(file.getParentFile());
                        EditorController.renameSelectedTab(path);
                    };
                });
            };
        });
    }
    
    private void newFile() {
        Globals.editorPane.newUntitledTab();
    }
}
