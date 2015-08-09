package winter.controllers.edits;

import winter.models.edits.FindModel;
import winter.models.edits.ReplaceModel;
import winter.views.edit.FindView;
import winter.views.edit.ReplaceView;
import winter.views.editor.EditorView;

import java.util.function.Function;

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
        replaceTemplate(source -> replaceModel.replace(source,
                getFindModel().getPosition(), getFindModel().getQueryString().length()));
    }

    @Override
    public void replaceAll() {
        replaceTemplate(source -> replaceModel.replaceAll(source, 
                getFindModel().getQueryString(), 
                replaceModel.getReplaceString(),
                getFindModel().isMatchCase(),
                getFindModel().isWords()));
    }
    
    private void replaceTemplate(Function<String, String> replaceFunction) {
        EditorView editorView = replaceView.getFindView().getFindController().getEditorController().getEditorView();
        String newContent = replaceFunction.apply(editorView.getText());
        editorView.replaceText(newContent);
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
