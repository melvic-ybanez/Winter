package winter.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/22/2015.
 */
public class Errors {
    public static Optional<ButtonType> exceptionDialog(String title, String headerText, String contentText, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exString = sw.toString();
        
        Label label = new Label("Exception stacktrace:");
        TextArea textArea = new TextArea(exString);
        textArea.setEditable(false);
        textArea.setStyle("-fx-text-fill: darkred");


        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(textArea);

        alert.getDialogPane().setExpandableContent(borderPane);

        // A hack to fix the resize issue of dialogs on linux
        alert.getDialogPane().expandedProperty().addListener(l -> {
            Platform.runLater(() -> {
                alert.getDialogPane().requestLayout();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.sizeToScene();
            });
        });

        return alert.showAndWait();
    }
    
    public static Optional<ButtonType> openFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, titles.OPEN_FILE, "opening the file");
    }
    
    public static Optional<ButtonType> saveFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, titles.SAVE_FILE, "saving the file");
    }
    
    public static Optional<ButtonType> moveFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, titles.MOVE_FILE, "moving the file");
    }

    public static Optional<ButtonType> deleteFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, titles.DELETE_FILE, "deleting the file");
    }

    public static Optional<ButtonType> addFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, titles.ADD_FILE, "adding the file");
    }

    public static Optional<ButtonType> addDirectoryExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, titles.ADD_DIRECTORY, "adding the directory");
    }

    public static Optional<ButtonType> renameFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, titles.RENAME_FILE, "renaming the file");
    }

    public static Optional<ButtonType> fileIOExceptionDialog(IOException ex, String title, String action) {
        return Errors.exceptionDialog(title + " Exception",
                "An exception has occurred while " + action + ".", ex.getMessage(), ex);
    }

    public static Optional<ButtonType> registerWatcherException(IOException ex) {
        return Errors.fileIOExceptionDialog(ex, titles.REGISTER_WATCHER, "registering watcher");
    }
    
    public static Optional<ButtonType> headerLessDialog(String title, String contents) {
        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
        errorDialog.setTitle(title);
        errorDialog.setHeaderText(null);
        errorDialog.setContentText(contents);
        return errorDialog.showAndWait();
    }
    
    public static class messages {
        public static String fileAlreadyExists(Path path) {
            return "File already exists: " + path;
        }
    }
    
    public static class titles {
        public static final String RENAME_FILE = "Rename File Error";
        public static final String DELETE_FILE = "Delete File Error";
        public static final String ADD_FILE = "Add File Error";
        public static final String ADD_DIRECTORY = "Add Directory Error";
        public static final String SAVE_FILE = "Save File Error";
        public static final String MOVE_FILE = "Move File Error";
        public static final String OPEN_FILE = "Open File Error";
        public static final String REGISTER_WATCHER = "Register Watcher Error";
    }
}
