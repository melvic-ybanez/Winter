package winter.models.edits;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ybamelcash on 7/21/2015.
 */
public class ReplaceModelImpl implements ReplaceModel {
    private StringProperty replaceStringProperty = new SimpleStringProperty("");
    
    @Override
    public String replace(String source, int from, int to) {
        if (from == -1) return source;
        String prePos = source.substring(0, from);
        String postPos = source.substring(from + to); 
        return prePos + getReplaceString() + postPos;
    }

    @Override
    public String replaceAll(String source, String oldString, String replacement, boolean matchCase, boolean words) {
        if (!matchCase) oldString = "(?i)" + oldString;
        if (words) oldString = "\\b" + oldString + "\\b";
        return source.replaceAll(oldString, replacement);
    }

    @Override
    public StringProperty replaceStringProperty() {
        return replaceStringProperty;
    }

    @Override
    public String getReplaceString() {
        return replaceStringProperty.get();
    }
}
