package winter.models;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class EditorModel {
    private Optional<Path> pathOpt = Optional.empty();
    private String contents;
    
    public EditorModel(Path path, String contents) {
        this.pathOpt = Optional.of(path);
        this.contents = contents;
    }
    
    public Optional<Path> getPath() {
        return pathOpt;
    }
    
    public String getContents() {
        return contents;
    }
    
    public boolean equalsPath(Path path) {
        return getPath().map(path1 -> path1.equals(path)).orElse(false);
    }
}
