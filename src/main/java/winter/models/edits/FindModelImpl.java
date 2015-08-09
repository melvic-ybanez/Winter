package winter.models.edits;

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
    private BooleanProperty wordsProperty = new SimpleBooleanProperty();
    private BooleanProperty matchCaseProperty = new SimpleBooleanProperty();
    private StringProperty queryStringProperty = new SimpleStringProperty("");
    private int position;
    
    public FindModelImpl() {
        resetPosition();
    }
    
    @Override
    public int findNext(String source) {
        if (position != -1) {
            setPosition(position % source.length() + getQueryString().length());
        }
        return find(source).apply((query, source1) -> {
            return source1.indexOf(query, getPosition());
        });
    }

    @Override
    public int findPrevious(String source) {
        if (position != -1) {
            setPosition(position % source.length() - 1);
        }
        return find(source).apply((query, source1) -> {
            return source1.lastIndexOf(query, getPosition() + source.length());
        });
    }
    
    private Function<BiFunction<String, String, Integer>, Integer> find(String source) {
        return func -> {
            if (getQueryString().isEmpty()) return -1;
            String queryString = isMatchCase() ? getQueryString() : getQueryString().toLowerCase();
            String newSource = isMatchCase() ? source : source.toLowerCase();
            newSource += newSource;
            
            int index = -1;
            if (wordsProperty.get()) {
                boolean isRightMost = false;
                
                while (!isRightMost) {
                    index = func.apply(queryString, newSource);
                    if (index == -1) break;
                    char left = ' ';
                    char right = ' ';

                    boolean isLeftMost = index % source.length() == 0;
                    if (!isLeftMost) {
                        left = newSource.charAt(index - 1);
                    }

                    int rightIndex = index + queryString.length();
                    isRightMost = rightIndex % source.length() == 0;
                    if (!isRightMost) {
                        right = newSource.charAt(rightIndex);
                    }

                    if (left == ' ' && right == ' ') break;     // We have found the word
                    index = -1; 
                }
            } else index = func.apply(queryString, newSource);
            
            index %= source.length();
            setPosition(index);
            
            return index;
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

    @Override
    public void resetPosition() {
        setPosition(-1);
    }
}
