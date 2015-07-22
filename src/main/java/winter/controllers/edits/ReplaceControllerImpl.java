package winter.controllers.edits;

import javafx.beans.property.IntegerProperty;
import winter.models.edits.FindModel;
import winter.models.edits.ReplaceModel;
import winter.utils.Observer;
import winter.views.edit.FindView;
import winter.views.edit.ReplaceView;
import winter.views.editor.EditorView;

/**
 * Created by ybamelcash on 7/21/2015.
 */
public class ReplaceControllerImpl implements ReplaceController {
    private ReplaceModel replaceModel;
    private ReplaceView replaceView;
    
    public ReplaceControllerImpl(ReplaceModel replaceModel, FindView findView) {
        setReplaceModel(replaceModel);
        setReplaceView(new ReplaceView(this, replaceModel, findView));
    }
    
    @Override
    public void replace() {
        EditorView editorView = replaceView.getFindView().getFindController().getEditorController().getEditorView();
        String newContent = replaceModel.replace(editorView.getText(),
                getFindModel().getPosition(), getFindModel().getQueryString().length());
        editorView.replaceText(newContent);
    }

    @Override
    public void replaceAll() {

    }

    public ReplaceModel getReplaceModel() {
        return replaceModel;
    }

    public void setReplaceModel(ReplaceModel replaceModel) {
        this.replaceModel = replaceModel;
    }

    public ReplaceView getReplaceView() {
        return replaceView;
    }

    public void setReplaceView(ReplaceView replaceView) {
        this.replaceView = replaceView;
    }
    
    public FindModel getFindModel() {
        return replaceView.getFindView().getFindModel();
    }
}
