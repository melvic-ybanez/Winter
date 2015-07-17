package winter.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by ybamelcash on 7/9/2015.
 */
public class FindModel {
    public static final SimpleBooleanProperty wordsProperty = new SimpleBooleanProperty();
    public static final SimpleBooleanProperty matchCaseProperty = new SimpleBooleanProperty();
    public static final SimpleStringProperty queryStringProperty = new SimpleStringProperty();
    
    public static boolean isWords() {
        return wordsProperty.get();
    }
    
    public static boolean isMatchCase() {
        return matchCaseProperty.get();
    }
    
    public static String getQueryString() {
        return queryStringProperty.get();
    }
}
