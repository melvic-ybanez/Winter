package winter.views.project;

import javafx.scene.control.Label;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public class ProjectNodeValue extends Label {
    private Optional<Path> pathOpt = Optional.empty();
    
    public ProjectNodeValue(Path path) {
        this.pathOpt = Optional.of(path);
        setText(path.getFileName().toString());
    }
    
    public ProjectNodeValue() { }
    
    public Optional<Path> getPath() {
        return pathOpt;
    }
}
