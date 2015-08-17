package winter.controllers.navigations;

import winter.controllers.editors.EditorController;
import winter.controllers.editors.EditorSetController;
import winter.models.editors.EditorModel;
import winter.utils.Errors;
import winter.utils.StringUtils;
import winter.views.RequiredTextInputDialog;
import winter.views.editor.EditorView;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Created by ybamelcash on 8/18/2015.
 */
public class NavigationControllerImpl implements NavigationController {
    private EditorSetController editorSetController;

    public NavigationControllerImpl(EditorSetController editorSetController) {
        setEditorSetController(editorSetController);
    }

    @Override
    public void goToFile() {

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
}
