package winter.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import winter.controllers.EditorsControllerImpl;
import winter.models.behaviors.*;
import winter.utils.*;
import winter.utils.Observable;
import winter.utils.Observer;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;

/**
 * Created by ybamelcash on 7/16/2015.
 */
public abstract class EditorModel implements Observable {
    private List<Observer> observers;
    private Either<Integer, Path> pathEither;
    private SimpleStringProperty contentsProperty = new SimpleStringProperty();
    private SimpleStringProperty titleProperty = new SimpleStringProperty();
    private SimpleIntegerProperty caretPositionProperty = new SimpleIntegerProperty();
    private String origContents = "";

    private ParenIndexesBehavior parenIndexesBehavior;
    private AutoIndentedLineStringBehavior autoIndentedLineStringBehavior;
    private MatchingParenIndexBehavior matchingParenIndexBehavior;
    private StyleClassBehavior styleClassBehavior;
    private ActiveParenIndexesBehavior activeParenIndexesBehavior;

    public EditorModel() {
        observers = new ArrayList<>();
    }

    public SimpleStringProperty contentsProperty() {
        return contentsProperty;
    }

    public SimpleIntegerProperty caretPositionProperty() {
        return caretPositionProperty;
    }

    public boolean equalsPath(Path path) {
        return getPath().map(path::equals).orElse(false);
    }

    public void setPathEither(Either<Integer, Path> pathEither) {
        this.pathEither = pathEither;
        this.titleProperty.setValue(pathEither.getRight()
                .map(path -> path.getFileName().toString())
                .orElseGet(() -> {
                    int untitledCount = pathEither.getLeft().get();
                    String suffix = untitledCount == 0 ? "" : untitledCount + "";
                    return Constants.UNTITLED + suffix;
                }));
    }

    public StyleSpans<Collection<String>> getStyleSpans(String text, int parenIndex1, int parenIndex2) {
        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
        int page = 0;
        int pageSize = text.length() > 1000 ? 1000 : text.length();
        String textToMatch = text.substring(page, page + pageSize);

        while (!textToMatch.isEmpty()) {
            Matcher matcher = MeruemEditorModel.PATTERN.matcher(textToMatch);
            int lastMatched = 0;

            while (matcher.find()) {
                String styleClass = getStyleClass(matcher, parenIndex1, parenIndex2);
                assert styleClass != null;
                builder.add(Collections.emptyList(), matcher.start() - lastMatched);
                builder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
                lastMatched = matcher.end();
            }
            builder.add(Collections.emptyList(), textToMatch.length() - lastMatched);

            page += pageSize;
            if (page == text.length()) break;
            if (page + pageSize > text.length()) {
                pageSize = text.length() - page;
            }
            textToMatch = text.substring(page, page + pageSize);
        }

        return builder.create();
    }

    public SimpleStringProperty titleProperty() {
        return titleProperty;
    }

    public String getLastLine() {
        String[] lines = getContents().split("\n", -1);
        if (lines.length == 0) return "";
        else return lines[lines.length - 1];
    }

    public Optional<Path> getPath() {
        return getPathEither().getRight();
    }

    public String getContents() {
        return contentsProperty().get();
    }

    public int getCaretPosition() {
        return caretPositionProperty().get();
    }

    public void setPath(Path path) {
        setPathEither(Either.right(path));
    }

    public boolean isUntitled() {
        return getPathEither().hasLeft();
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public void ifUntitled(Consumer<Integer> f) {
        getPathEither().getLeft().ifPresent(f);
    }

    public void save() {
        setOrigContents(getContents());
        EditorsControllerImpl.updateTabGraphic();
    }
    
    public boolean unsaved() {
        return !getContents().equals(origContents);
    }

    public Either<Integer, Path> getPathEither() {
        return pathEither;
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void setOrigContents(String contents) {
        origContents = contents;
    }

    public String getOrigContents() {
        return origContents;
    }

    public boolean withParenHead(String str, char paren) {
        if (str.isEmpty()) return false;
        else return str.charAt(0) == paren;
    }

    public void setParenIndexesBehavior(ParenIndexesBehavior parenIndexesBehavior) {
        this.parenIndexesBehavior = parenIndexesBehavior;
    }

    public void setAutoIndentedLineStringBehavior(AutoIndentedLineStringBehavior autoIndentedLineStringBehavior) {
        this.autoIndentedLineStringBehavior = autoIndentedLineStringBehavior;
    }

    public void setMatchingParenIndexBehavior(MatchingParenIndexBehavior matchingParenIndexBehavior) {
        this.matchingParenIndexBehavior = matchingParenIndexBehavior;
    }

    public void setStyleClassBehavior(StyleClassBehavior styleClassBehavior) {
        this.styleClassBehavior = styleClassBehavior;
    }

    public void setActiveParenIndexesBehavior(ActiveParenIndexesBehavior activeParenIndexesBehavior) {
        this.activeParenIndexesBehavior = activeParenIndexesBehavior;
    }

    public ParenIndexesBehavior getParenIndexesBehavior() {
        return parenIndexesBehavior;
    }

    public AutoIndentedLineStringBehavior getAutoIndentedLineStringBehavior() {
        return autoIndentedLineStringBehavior;
    }

    public MatchingParenIndexBehavior getMatchingParenIndexBehavior() {
        return matchingParenIndexBehavior;
    }

    public StyleClassBehavior getStyleClassBehavior() {
        return styleClassBehavior;
    }

    public ActiveParenIndexesBehavior getActiveParenIndexesBehavior() {
        return activeParenIndexesBehavior;
    }

    public abstract Optional<Pair<Integer, Integer>> getParenIndexes(char paren);
    
    public abstract Optional<Integer> getMatchingParenIndex(String str, char caretParen, char matchingParen);

    public abstract String getAutoIndentedNewLineString();

    public abstract Optional<Pair<Integer, Integer>> getActiveParenIndexes();
    
    public abstract String getStyleClass(Matcher matcher, int parenIndex1, int parenIndex2);
}
