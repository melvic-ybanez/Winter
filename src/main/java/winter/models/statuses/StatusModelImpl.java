package winter.models.statuses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by ybamelcash on 8/2/2015.
 */
public class StatusModelImpl implements StatusModel {
    private IntegerProperty lineNumberProperty = new SimpleIntegerProperty();
    private IntegerProperty columnNumberProperty = new SimpleIntegerProperty();
    
    @Override
    public IntegerProperty lineNumberProperty() {
        return lineNumberProperty;
    }

    @Override
    public IntegerProperty columnNumberProperty() {
        return columnNumberProperty;
    }

    @Override
    public int getLineNumber() {
        return lineNumberProperty.get();
    }

    @Override
    public int getColumnNumber() {
        return columnNumberProperty.get();
    }
}
