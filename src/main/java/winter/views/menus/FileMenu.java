package winter.views.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import winter.controllers.FileController;
import winter.controllers.ProjectController;
import winter.utils.Either;
import winter.views.Errors;
import winter.views.Settings;
import winter.views.EditorPane;
import winter.Globals;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class FileMenu extends Menu {
    private MenuItem openMenuItem = new MenuItem("Open...");
    private MenuItem openFolderItem = new MenuItem("Open folder...");
    private FileChooser fileChooser = new FileChooser();
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    
    public FileMenu() {
        super("File");
        getItems().addAll(openMenuItem, openFolderItem);
        registerEventHandlers();
    }
    
    private void registerEventHandlers() {
        openMenuItem.setOnAction(event -> openFile());
        openFolderItem.setOnAction(event -> openFolder());
    }
    
    private void openFile() {
        fileChooser.setTitle("Open File");

        Settings.SUPPORTED_FILE_FORMATS.forEach(format -> {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    format.getString("description"),
                    "*" + format.getString("extension")));
        });

        Optional.ofNullable(fileChooser.showOpenDialog(Globals.getMainStage())).ifPresent(file -> {
            Either<IOException, String> result = FileController.openFile(file.toPath());
            result.getLeft().ifPresent(ex -> {
                Errors.exceptionDialog("Open File Exception",
                        "An exception has occurred while opening the file", ex.getMessage(), ex);
            });
            result.getRight().ifPresent(contents -> {
                Globals.editorPane.newEditorAreaTab(file.getName(), contents);
            });
        });
    }
    
    private void openFolder() {
        directoryChooser.setTitle("Open Folder");
        Optional.ofNullable(directoryChooser.showDialog(Globals.getMainStage())).ifPresent(file -> {
            Globals.projectsPane.displayProject(file.toPath());
        });
    }
}
