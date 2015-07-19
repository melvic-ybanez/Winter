package winter.controllers;

import org.fxmisc.richtext.CodeArea;
import winter.models.EditorModel;
import winter.models.FindModel;
import winter.views.edit.FindView;
import winter.views.editors.EditorView;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by ybamelcash on 7/19/2015.
 */
public class FindControllerImpl implements FindController {
    private FindModel findModel;
    private FindView findView;
    private EditorSetController editorSetController;
    
    public FindControllerImpl(FindModel findModel, EditorSetController editorSetController) {
        setFindView(new FindView(this, findModel));
        this.findModel = findModel;
        this.editorSetController = editorSetController;
    }
            
    @Override
    public void findNext() {
        find(findModel::findNext);
    }

    @Override
    public void findPrevious() {
        find(findModel::findPrevious);
    }
    
    private void find(Function<String, Integer> findFunction) {
        EditorView editorView = editorSetController.getActiveEditorView(); 
        String contents = editorView.getText();
        int selectPosition = findFunction.apply(contents);
        if (selectPosition == -1) {
            findView.displayNoMatchDialog();
        } else {
            int queryLength = findModel.getQueryString().length();
            editorView.positionCaret(selectPosition);
            editorView.setStyleClass(selectPosition, selectPosition + queryLength, "find-highlight");
        }
    }

    public FindView getFindView() {
        return findView;
    }

    public void setFindView(FindView findView) {
        this.findView = findView;
    }
}
