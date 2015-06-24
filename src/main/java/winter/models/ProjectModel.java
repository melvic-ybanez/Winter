package winter.models;

import winter.utils.Either;
import winter.views.ProjectsPane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by ybamelcash on 6/23/2015.
 */
public class ProjectModel {
    private Path path;
    
    public ProjectModel(Path path) {
        this.path = path;
    }
    
    public Either<String, Path> addFile(Path path) {
        if (Files.exists(path)) {
            return Either.left("File already exists");
        } else {
            try {
                return Either.right(Files.createFile(path));
            } catch (IOException e) {
                return Either.left(e.getMessage());
            }
        } 
    }
    
    public Path getPath() {
        return path;
    }
}
