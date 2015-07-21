package winter.controllers.edits;

import winter.controllers.editors.EditorController;
import winter.models.edits.FindModel;
import winter.views.edit.FindView;
import winter.views.editor.EditorView;

import java.util.function.Function;

/**
 * Created by ybamelcash on 7/19/2015.
 */
public class FindControllerImpl implements FindController {
    private FindModel findModel;
    private FindView findView;
    private EditorController editorController;
    
    public FindControllerImpl(FindModel findModel, EditorController editorController) {
        setFindView(new FindView(this, findModel));
        this.findModel = findModel;
        this.editorController = editorController;
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
        EditorView editorView = editorController.getEditorView(); 
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

    @Override
    public FindModel getFindModel() {
        return findModel;
    }
    
    public void showFindView() {
        findView.showUI();
    }
}
