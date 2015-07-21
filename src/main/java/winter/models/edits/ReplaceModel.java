package winter.models.edits;

import javafx.beans.property.StringProperty;

/**
 * Created by ybamelcash on 7/21/2015.
 */
public interface ReplaceModel {
    public String replace(String source, int from, int to);
    
    public String replaceAll(String source, String oldString, String replacement, boolean matchCase, boolean words);
    
    public StringProperty replaceStringProperty();
    
    public String getReplaceString();
}
