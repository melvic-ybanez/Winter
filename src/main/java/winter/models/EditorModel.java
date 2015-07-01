package winter.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import winter.utils.Either;
import winter.utils.Pair;
import winter.utils.StringUtils;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Stack;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class EditorModel {
    private Either<String, Path> pathEither;
    private SimpleStringProperty contentsProperty = new SimpleStringProperty();
    private SimpleStringProperty titleProperty = new SimpleStringProperty();
    private SimpleIntegerProperty caretPositionProperty = new SimpleIntegerProperty();
    
    public EditorModel(Either<String, Path> pathEither) {
        setPathEither(pathEither);
    }
    
    public Optional<Path> getPath() {
        return pathEither.getRight();
    }
    
    public SimpleStringProperty contentsProperty() {
        return contentsProperty;
    }
    
    public SimpleIntegerProperty caretPositionProperty() {
        return caretPositionProperty;
    }
    
    public String getContents() {
        return contentsProperty().getValue();
    }
    
    public int getCaretPosition() {
        return caretPositionProperty.get();
    }
    
    public void setPath(Path path) {
        setPathEither(Either.right(path));
    }
    
    public boolean equalsPath(Path path) {
        return getPath().map(path1 -> path1.equals(path)).orElse(false);
    }
    
    public void setPathEither(Either<String, Path> pathEither) {
        this.pathEither = pathEither;
        this.titleProperty.setValue(pathEither.getRight()
                .map(path -> path.getFileName().toString())
                .orElseGet(() -> pathEither.getLeft().get()));
    }
    
    public void setContents(SimpleStringProperty contents) {
        this.contentsProperty = contents;
    }
    
    public SimpleStringProperty titleProperty() {
        return titleProperty;
    }
    
    public String getLastLine() {
        String[] lines = getContents().split("\n", -1);
        if (lines.length == 0) return "";
        else return lines[lines.length - 1];
    }
    
    public Optional<Pair<Integer, Integer>> getParenIndexes(char paren) {
        int caretPos = getCaretPosition();
        Pair<String, String> pair = StringUtils.splitAt(getContents(), caretPos);
        String first = pair.getFirst();
        String second = pair.getSecond();
        
        // check which side the paren is located
        String lastOfFirst = first.isEmpty() ? "" : first.substring(first.length() - 1);
        boolean parenFoundAtLeft = getParenHeadValue(lastOfFirst, paren);
        boolean parenFoundAtRight = getParenHeadValue(second, paren);
        
        Optional<Integer> parenToMatchIndexOpt = Optional.empty();
        if (parenFoundAtLeft) {
            parenToMatchIndexOpt = Optional.of(caretPos - 1);       // move backward
        } else if (parenFoundAtRight) {
            parenToMatchIndexOpt = Optional.of(caretPos);
        }
        
        return parenToMatchIndexOpt.flatMap(parenToMatchIndex -> {
            Optional<Integer> matchIndexOpt = Optional.empty();
            if (paren == '(') {
                // Remove the first paren from the second string.
                String second1 = parenFoundAtRight ? second.substring(1) : second;
                
                // Add one to delta if the first paren was removed to fill in the missing space.
                int delta = parenFoundAtRight? 1 : 0;       
                
                // Move delta steps forward
                matchIndexOpt = getMatchingParenIndex(second1, paren, ')').map(i -> i + caretPos + delta);
            } else if (paren == ')') {
                // The traversal here starts at the end since this is to find the opening paren.
                String reversedFirst = new StringBuilder(first).reverse().toString();
                
                // Apply the same logic as above (with a different predicate).
                String first1 = parenFoundAtLeft ? reversedFirst.substring(1) : reversedFirst;
                int delta = parenFoundAtLeft ? 1 : 0;
                
                // Move delta steps backward, minus one (due to the nature of the caret placement).
                matchIndexOpt = getMatchingParenIndex(first1, paren, '(').map(i -> caretPos - i - 1 - delta);
            } 
            
            return matchIndexOpt.flatMap(matchIndex -> {
                return Optional.of(Pair.of(parenToMatchIndex, matchIndex));    
            });
        });
    }
    
    private Optional<Integer> getMatchingParenIndex(String str, char caretParen, char matchingParen) {
        Stack<Character> parenStack = new Stack<>();
        Optional<Integer> matchingIndexOpt = Optional.empty();
        
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == caretParen) {
                parenStack.push(c);
            } else if (c == matchingParen) {
                if (parenStack.isEmpty()) {
                    matchingIndexOpt = Optional.of(i);
                    break;
                } else {
                    parenStack.pop();
                }
            }
        }
        
        return matchingIndexOpt;
    }
    
    private boolean getParenHeadValue(String str, char paren) {
        if (str.isEmpty()) return false;
        else return str.charAt(0) == paren;
    }
}
