package winter.views.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import winter.controllers.FileController;
import winter.utils.Either;
import winter.utils.Errors;
import winter.views.Settings;
import winter.Globals;

import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class FileMenu extends Menu {
    private FileChooser fileChooser = new FileChooser();
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    
    public FileMenu() {
        super("File");
        init();
    }
    
    private void init() {
        MenuItem newFileItem = new MenuItem("New File");
        MenuItem openFileItem = new MenuItem("Open File...");
        MenuItem openFolderItem = new MenuItem("Open Folder...");

        openFileItem.setOnAction(event -> openFile());
        openFolderItem.setOnAction(event -> openFolder());
        newFileItem.setOnAction(event -> newFile());
        
        getItems().addAll(newFileItem, openFileItem, openFolderItem);
    }
    
    private void openFile() {
        fileChooser.setTitle("Open File");

        Settings.SUPPORTED_FILE_FORMATS.forEach(format -> {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    format.getString("description"),
                    "*" + format.getString("extension")));
        });

        Optional.ofNullable(fileChooser.showOpenDialog(Globals.getMainStage())).ifPresent(file -> {
            Path path = file.toPath(); 
            Either<IOException, String> result = FileController.openFile(path);
            result.getLeft().ifPresent(Errors::openFileException);
            result.getRight().ifPresent(contents -> {
                Globals.editorPane.newEditorAreaTab(path, contents);
            });
            fileChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    private void openFolder() {
        directoryChooser.setTitle("Open Folder");
        Optional.ofNullable(directoryChooser.showDialog(Globals.getMainStage())).ifPresent(file -> {
            Globals.projectsPane.displayProject(file.toPath());
            directoryChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    private void newFile() {
        Globals.editorPane.createUntitledTab();
    }
}
