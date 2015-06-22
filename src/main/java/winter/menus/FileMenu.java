package winter.menus;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import winter.EditorPane;
import winter.Errors;
import winter.Globals;
import winter.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class FileMenu extends Menu {
    private MenuItem openMenuItem = new MenuItem("Open...");
    private EditorPane editorPane;
    
    public FileMenu(EditorPane editorPane) {
        super("File");
        this.editorPane = editorPane;
        getItems().addAll(openMenuItem);
        registerEventHandlers();
    }
    
    private void registerEventHandlers() {
        openMenuItem.setOnAction((ActionEvent t) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            
            Settings.SUPPORTED_FILE_FORMATS.forEach(format -> {
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                        format.getString("description"),
                        "*" + format.getString("extension")));
            });
            
            Optional.ofNullable(fileChooser.showOpenDialog(Globals.getMainStage())).ifPresent(file -> {
                String contents = "";
                try (BufferedReader reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset())) {
                    while (true) {
                        Optional<String> lineOpt = Optional.ofNullable(reader.readLine());
                        if (lineOpt.isPresent()) {
                            contents += lineOpt.get() + "\n";
                        } else {
                            if (!contents.isEmpty()) {
                                // remove the extra newline character
                                contents = contents.substring(0, contents.length() - 1);
                            } 
                            break;
                        }
                    }
                } catch (IOException ex) {
                    Errors.exceptionDialog("IOException", "An exception has occurred", ex.getMessage(), ex);
                }
                editorPane.newEditorAreaTab(file.getName(), contents);
            });
        });
    }
}
