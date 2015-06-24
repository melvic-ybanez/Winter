package winter.views.projects;

import javafx.scene.control.Label;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public class ProjectNodeValue extends Label {
    private Path path;
    
    public ProjectNodeValue(Path path) {
        this.path = path;
        setText(path.getFileName().toString());
    }
    
    public Path getPath() {
        return path;
    }
}
