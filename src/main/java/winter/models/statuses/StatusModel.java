package winter.models.statuses;

import javafx.beans.property.IntegerProperty;
import winter.controllers.editors.EditorSetController;
import winter.utils.Observable;

/**
 * Created by ybamelcash on 8/1/2015.
 */
public interface StatusModel extends Observable {
    public IntegerProperty lineNumberProperty();
    
    public IntegerProperty columnNumberProperty();
    
    public int getLineNumber();
    
    public int getColumnNumber();
    
    public void setEditorSetController(EditorSetController editorSetController);
    
    public EditorSetController getEditorSetController();
    
    public boolean areChangesSaved();
}
