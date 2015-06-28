package winter.models;

import javafx.beans.property.SimpleStringProperty;
import winter.utils.Either;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class EditorModel {
    private Either<String, Path> pathEither;
    private SimpleStringProperty contents;
    private SimpleStringProperty title = new SimpleStringProperty();
    
    public EditorModel(Either<String, Path> pathEither, SimpleStringProperty contents) {
        setPathEither(pathEither);
        setContents(contents);
    }
    
    public Optional<Path> getPath() {
        return pathEither.getRight();
    }
    
    public SimpleStringProperty getContentsProperty() {
        return contents;
    }
    
    public String getContents() {
        return getContentsProperty().getValue();
    }
    
    public void setPath(Path path) {
        setPathEither(Either.right(path));
    }
    
    public boolean equalsPath(Path path) {
        return getPath().map(path1 -> path1.equals(path)).orElse(false);
    }
    
    public void setPathEither(Either<String, Path> pathEither) {
        this.pathEither = pathEither;
        this.title.setValue(pathEither.getRight()
                .map(path -> path.getFileName().toString())
                .orElseGet(() -> pathEither.getLeft().get()));
    }
    
    public void setContents(SimpleStringProperty contents) {
        this.contents = contents;
    }
    
    public SimpleStringProperty getTitleProperty() {
        return title;
    }
}
