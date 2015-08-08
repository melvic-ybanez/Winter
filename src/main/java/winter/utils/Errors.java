package winter.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

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
        textArea.setWrapText(true);


        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(textArea);

        alert.getDialogPane().setExpandableContent(borderPane);

        return alert.showAndWait();
    }
    
    public static Optional<ButtonType> openFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, "Open File", "opening the file");
    }
    
    public static Optional<ButtonType> saveFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, "Save File", "saving the file");
    }
    
    public static Optional<ButtonType> moveFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, "Move File", "moving the file");
    }

    public static Optional<ButtonType> deleteFileExceptionDialog(IOException ex) {
        return fileIOExceptionDialog(ex, titles.DELETE, "deleting the file");
    }

    public static Optional<ButtonType> fileIOExceptionDialog(IOException ex, String title, String action) {
        return Errors.exceptionDialog(title + " Exception",
                "An exception has occurred while " + action + ".", ex.getMessage(), ex);
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
        public static final String RENAME = "Rename File Error";
        public static final String DELETE = "Delete File Error";
    }
}
