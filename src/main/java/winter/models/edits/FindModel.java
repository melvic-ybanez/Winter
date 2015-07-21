package winter.models.edits;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ybamelcash on 7/19/2015.
 */
public interface FindModel {
    public int findNext(String str);
    
    public int findPrevious(String str);
    
    public BooleanProperty wordsProperty();
    
    public BooleanProperty matchCaseProperty();
    
    public StringProperty queryStringProperty();
    
    public IntegerProperty positionProperty();
    
    public String getQueryString();
    
    public boolean isWords();
    
    public boolean isMatchCase();
    
    public void setPosition(int position);
    
    public int getPosition();
}
