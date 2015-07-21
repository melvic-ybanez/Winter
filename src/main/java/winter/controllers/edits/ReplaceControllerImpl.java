package winter.controllers.edits;

import winter.models.edits.FindModel;
import winter.models.edits.ReplaceModel;
import winter.views.edit.FindView;
import winter.views.edit.ReplaceView;

/**
 * Created by ybamelcash on 7/21/2015.
 */
public class ReplaceControllerImpl implements ReplaceController {
    private ReplaceModel replaceModel;
    private ReplaceView replaceView;
    
    public ReplaceControllerImpl(ReplaceModel replaceModel, FindView findView) {
        setReplaceModel(replaceModel);
        setReplaceView(new ReplaceView(findView));
    }
    
    @Override
    public void replace() {
        FindModel findModel = replaceView.getFindView().getFindModel();
        
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
}
