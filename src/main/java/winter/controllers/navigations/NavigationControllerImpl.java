package winter.controllers.navigations;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import winter.controllers.editors.EditorController;
import winter.controllers.editors.EditorSetController;
import winter.models.editors.EditorModel;
import winter.utils.Errors;
import winter.utils.Pair;
import winter.utils.StreamUtils;
import winter.utils.StringUtils;
import winter.views.RequiredTextInputDialog;
import winter.views.editor.EditorView;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static winter.utils.StreamUtils.mapToList;

/**
 * Created by ybamelcash on 8/18/2015.
 */
public class NavigationControllerImpl implements NavigationController {
    private EditorSetController editorSetController;
    private Stage goToFilePopup;
    private Optional<Path> prevPathOpt = Optional.empty();

    public NavigationControllerImpl(EditorSetController editorSetController) {
        setEditorSetController(editorSetController);
    }

    @Override
    public void goToFile() {
        EditorView editorView = editorSetController.getActiveEditorView();

        goToFilePopup = new Stage();
        goToFilePopup.initStyle(StageStyle.UNDECORATED);

        TextField filenameField = new TextField();
        ListView<VBox> filesView = new ListView<>();
        List<EditorController> editorControllers = editorSetController.getEditorSetView().getEditorControllers();

        prevPathOpt.ifPresent(prevPath -> filenameField.setText(prevPath.toString()));
        populateFilesView(filesView, mapToList(editorControllers.stream(), EditorController::getEditorModel));

        BorderPane mainPane = new BorderPane();
        mainPane.setTop(filenameField);
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

        filenameField.setOnKeyReleased(event -> {
            String filename = filenameField.getText().trim();
            List<Pair<EditorModel, Integer>> editorItems = StreamUtils.mapToList(editorControllers.stream(), editorController -> {
                EditorModel model = editorController.getEditorModel();
                String name = model.getTitle();

                int value = 0;
                int j = 0;
                for (int i = 0; i < name.length(); i++) {
                    if (j == filename.length()) break;

                    char c1 = filename.charAt(j);
                    char c2 = name.charAt(i);

                    if (Character.toLowerCase(c1) == Character.toLowerCase(c2)) {
                        value++;
                        if (i == j) value++;
                        if (c1 == c2) value++;
                        j++;
                    }
                }

                return Pair.of(model, value);
            });

            Collections.sort(editorItems, (x, y) -> {
                Integer xValue = x.getSecond();
                Integer yValue = y.getSecond();
                return yValue.compareTo(xValue);
            });

            populateFilesView(filesView, mapToList(editorItems.stream(), Pair::getFirst));
        });

        filenameField.setOnAction(e -> {
            String filename = filenameField.getText().trim();
            if (StreamUtils.exists(editorControllers.stream(), editorController -> {
                EditorModel editorModel = editorController.getEditorModel();
                return editorModel.getTitle().equals(filename);
            })) {

            }
        });
    }

    private void populateFilesView(ListView<VBox> filesView, List<EditorModel> editorModels) {
        filesView.getItems().clear();
        IntStream.range(0, editorModels.size()).forEach(i -> {
            EditorModel editorModel = editorModels.get(i);
            String title = editorModel.getTitle();
            VBox pane = new VBox();

            Label titleLabel = new Label(title);
            Label pathLabel = new Label();

            int titleSize = 13;
            String titleDefaultStyle = "-fx-font-size: " + titleSize + ";";

            titleLabel.setStyle(titleDefaultStyle);
            if (i == 0) {
                titleLabel.setStyle(titleDefaultStyle + "-fx-font-weight: bold");
            }
            pathLabel.setStyle("-fx-font-style: italic; " +
                    "-fx-font-size: " + (titleSize - 1) + "; " +
                    "-fx-text-fill: gray");

            if (editorModel.isUntitled()) {
                pathLabel.setText("Path not available");
            } else {
                Path path = editorModel.getPath().get();
                pathLabel.setText(path.toString());
            }

            pane.getChildren().addAll(titleLabel, pathLabel);
            filesView.getItems().add(pane);
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
