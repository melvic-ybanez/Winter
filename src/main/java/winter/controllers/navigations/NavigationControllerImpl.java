package winter.controllers.navigations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import winter.controllers.editors.EditorController;
import winter.controllers.editors.EditorSetController;
import winter.models.editors.EditorModel;
import winter.utils.Errors;
import winter.utils.Pair;
import winter.utils.StringUtils;
import winter.views.RequiredTextInputDialog;
import winter.views.editor.EditorView;
import winter.views.navigation.NavigationView;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static winter.utils.StreamUtils.*;

/**
 * Created by ybamelcash on 8/18/2015.
 */
public class NavigationControllerImpl implements NavigationController {
    private EditorSetController editorSetController;
    private NavigationView navigationView;

    public NavigationControllerImpl(EditorSetController editorSetController) {
        setEditorSetController(editorSetController);
    }

    @Override
    public void showGoToFileUI() {
        if (navigationView == null) {
            navigationView = new NavigationView("", this, editorSetController);
        }
        List<EditorController> editorControllers = editorSetController.getEditorSetView().getEditorControllers();
        List<EditorModel> editorModels = mapToList(editorControllers.stream(), EditorController::getEditorModel);
        navigationView.getFilenameField().clear();
        populateFilesView(editorModels);
        navigationView.show();
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
    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    public void populateFilesView(List<EditorModel> editorModels) {
        navigationView.getFilesView().setItems(FXCollections.observableArrayList(editorModels));
    }

    @Override
    public void filenameAutoCompleteOnType() {
        TextField filenameField = navigationView.getFilenameField();
        List<EditorController> editorControllers = editorSetController.getEditorSetView().getEditorControllers();

        String filename = filenameField.getText().trim();
        Stream<Pair<EditorModel, Integer>> valuedModels = editorControllers.stream().map(editorController -> {
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

            if (j < filename.length()) value = 0;

            return Pair.of(model, value);
        });

        List<Pair<EditorModel, Integer>> editorItems = filterToList(valuedModels,
                filename.isEmpty() ? x -> true : x -> x.getSecond() > 0);

        Collections.sort(editorItems, (x, y) -> {
            Integer xValue = x.getSecond();
            Integer yValue = y.getSecond();
            return yValue.compareTo(xValue);
        });

        populateFilesView(mapToList(editorItems.stream(), Pair::getFirst));
    }

    @Override
    public void selectFilename() {
        ObservableList<EditorModel> items = navigationView.getFilesView().getItems();
        if (items.isEmpty()) {
            return;
        }
        selectFilename(items.get(0));
    }

    @Override
    public void selectFilename(EditorModel editorModel) {
        editorSetController.selectTab(editorModel);
        navigationView.close();
    }
}
