package winter.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by ybamelcash on 7/9/2015.
 */
public class FindModelImpl implements FindModel {
    private SimpleBooleanProperty wordsProperty = new SimpleBooleanProperty();
    private SimpleBooleanProperty matchCaseProperty = new SimpleBooleanProperty();
    private SimpleStringProperty queryStringProperty = new SimpleStringProperty("");
    private int position = 0;
    

    @Override
    public int findNext(String source) {
        return find(source).apply((query, source1) -> {
            int result = source1.indexOf(query, position);
            setPosition(result % source.length() + query.length());
            return result;
        });
    }

    @Override
    public int findPrevious(String source) {
        return find(source).apply((query, source1) -> {
            int result = source1.lastIndexOf(query, position + source.length());
            setPosition(result % source.length() - 1);
            return result;
        });
    }
    
    private Function<BiFunction<String, String, Integer>, Integer> find(String source) {
        return func -> {
            if (getQueryString().isEmpty()) return -1;
            String queryString = isMatchCase() ? getQueryString() : getQueryString().toLowerCase();
            String source1 = isMatchCase() ? source : source.toLowerCase();
            
            int index = func.apply(queryString, source1 + source1);
            return index % source.length();
        };
    }

    public boolean isWords() {
        return wordsProperty.get();
    }

    public boolean isMatchCase() {
        return matchCaseProperty.get();
    }

    public String getQueryString() {
        return queryStringProperty.get();
    }
    
    public BooleanProperty wordsProperty() {
        return wordsProperty;
    }
    
    public BooleanProperty matchCaseProperty() {
        return matchCaseProperty;
    }
    
    public StringProperty queryStringProperty() {
        return queryStringProperty;
    }

    @Override
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
