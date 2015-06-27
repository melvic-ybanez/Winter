package winter.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    
    public static Optional<ButtonType> openFileException(IOException ex) {
        return Errors.exceptionDialog("Open File Exception",
                "An exception has occurred while opening the file", ex.getMessage(), ex);
    }
    
    public static Optional<ButtonType> saveFileException(IOException ex) {
        return Errors.exceptionDialog("Save File Exception",
                "An exception has occured while saving the file", ex.getMessage(), ex);
    }
}
