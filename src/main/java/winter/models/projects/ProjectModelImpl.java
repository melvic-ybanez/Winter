package winter.models.projects;

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
        return path.getFileName().toString();
    }
}
