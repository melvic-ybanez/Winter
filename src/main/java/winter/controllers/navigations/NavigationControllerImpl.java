package winter.controllers.navigations;

import com.sun.javafx.event.RedirectedEvent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import winter.controllers.editors.EditorController;
import winter.controllers.editors.EditorSetController;
import winter.models.editors.EditorModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.StringUtils;
import winter.views.RequiredTextInputDialog;
import winter.views.editor.EditorView;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ybamelcash on 8/18/2015.
 */
public class NavigationControllerImpl implements NavigationController {
    private EditorSetController editorSetController;
    private Stage goToFilePopup;

    public NavigationControllerImpl(EditorSetController editorSetController) {
        setEditorSetController(editorSetController);
    }

    @Override
    public void goToFile() {
        EditorView editorView = editorSetController.getActiveEditorView();
        goToFilePopup = new Stage();
        goToFilePopup.initStyle(StageStyle.UNDECORATED);
        TextField fileField = new TextField();
        ListView<String> filesView = new ListView<>();
        List<EditorController> editorControllers = editorSetController.getEditorSetView().getEditorControllers();
        editorControllers.stream().forEach(editorController -> {
            EditorModel editorModel = editorController.getEditorModel();
            String title = editorModel.getTitle();
            Either<Integer, Path> pathEither = editorModel.getPathEither();
            filesView.getItems().add(title);
        });

        BorderPane mainPane = new BorderPane();
        mainPane.setTop(fileField);
        mainPane.setBottom(filesView);

        Scene scene = new Scene(mainPane);

        goToFilePopup.setScene(scene);
        goToFilePopup.initOwner(editorView.getScene().getWindow());
        goToFilePopup.show();

        goToFilePopup.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                goToFilePopup.close();
            }
        });
    }

    @Override
    public void goToLine() {
        EditorModel activeEditorModel = editorSetController.getActiveEditorModel();
        RequiredTextInputDialog dialog = new RequiredTextInputDialog((activeEditorModel.getLineNumber()+ 1) + "");
        dialog.setContentText("Line number:");
        Optional<String> answer = dialog.getAnswer();
        answer.ifPresent(lineNumberStr -> {
            EditorView editorView = editorSetController.getActiveEditorView();
            Optional<Integer> lineNumberOpt = StringUtils.asInteger(lineNumberStr);

            Consumer<String> displayError = lineNumberString ->
                    Errors.headerLessDialog("Go To Line Error", "Invalid Line Number: " + lineNumberString);

            if (!lineNumberOpt.isPresent()) {
                displayError.accept(lineNumberStr);
            }
            lineNumberOpt.ifPresent(lineNumber -> {
                if (lineNumber < 0) {
                    displayError.accept(lineNumber + "");
                }
                String contents = activeEditorModel.getContents();
                int currentLine = 1;
                int position = 0;
                for (int i = 0; i < contents.length(); i++) {
                    if (currentLine == lineNumber) {
                        position = i;
                        break;
                    }
                    if (contents.charAt(i) == '\n') {
                        currentLine++;
                    }
                }
                editorView.positionCaret(position);
            });
        });
    }

    public EditorSetController getEditorSetController() {
        return editorSetController;
    }

    public void setEditorSetController(EditorSetController editorSetController) {
        this.editorSetController = editorSetController;
    }

    @Override
    public Stage getGoToFilePopup() {
        return goToFilePopup;
    }
}
