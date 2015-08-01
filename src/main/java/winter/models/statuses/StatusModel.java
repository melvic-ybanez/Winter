package winter.models.statuses;

import javafx.beans.property.IntegerProperty;

/**
 * Created by ybamelcash on 8/1/2015.
 */
public interface StatusModel {
    public IntegerProperty lineNumberProperty();
    
    public IntegerProperty columnNumberProperty();
    
    public int getLineNumber();
    
    public int getColumnNumber();
}
