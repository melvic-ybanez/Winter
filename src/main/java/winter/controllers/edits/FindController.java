package winter.controllers.edits;

import winter.models.edits.FindModel;
import winter.views.edit.FindView;

/**
 * Created by ybamelcash on 7/19/2015.
 */
public interface FindController {
    public void findNext();
    
    public void findPrevious();
    
    public FindView getFindView();
    
    public void setFindView(FindView findView);
    
    public FindModel getFindModel();
    
    public void showFindView();
}
