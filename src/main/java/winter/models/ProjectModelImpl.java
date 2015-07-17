package winter.models;

import winter.utils.Either;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by ybamelcash on 6/23/2015.
 */
public class ProjectModelImpl implements ProjectModel {
    private Path path;
    
    public ProjectModelImpl(Path path) {
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
