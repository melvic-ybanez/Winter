package winter.models;

import javafx.beans.property.*;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by ybamelcash on 7/9/2015.
 */
public class FindModelImpl implements FindModel {
    private BooleanProperty wordsProperty = new SimpleBooleanProperty();
    private BooleanProperty matchCaseProperty = new SimpleBooleanProperty();
    private StringProperty queryStringProperty = new SimpleStringProperty("");
    private IntegerProperty positionProperty = new SimpleIntegerProperty(0);
    
    public FindModelImpl(int initialPosition) {
        setPosition(initialPosition);
    }
    
    @Override
    public int findNext(String source) {
        return find(source).apply((query, source1) -> {
            int result = source1.indexOf(query, getPosition());
            setPosition(result % source.length() + query.length());
            return result;
        });
    }

    @Override
    public int findPrevious(String source) {
        return find(source).apply((query, source1) -> {
            int result = source1.lastIndexOf(query, getPosition() + source.length());
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
    public IntegerProperty positionProperty() {
        return positionProperty;
    }

    @Override
    public int getPosition() {
        return positionProperty.get();
    }

    public void setPosition(int position) {
        this.positionProperty.set(position);
    }
}
