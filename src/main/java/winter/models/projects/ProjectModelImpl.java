package winter.models.projects;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 8/7/2015.
 */
public class ProjectModelImpl implements ProjectModel {
    private Path path;
    
    public ProjectModelImpl(Path path) {
        this.path = path;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getName() {
        Path filename = path.getFileName();
        return filename == null ? "" : filename.toString();
    }
}
