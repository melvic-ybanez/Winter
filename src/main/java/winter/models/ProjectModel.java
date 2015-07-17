package winter.models;

import winter.utils.Either;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 7/16/2015.
 */
public interface ProjectModel {
    public Either<String, Path> addFile(Path path);
    
    public Path getPath();
}
