package winter.models.projects;

import winter.utils.Pair;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 7/16/2015.
 */
public interface ProjectModel {
    public Optional<Path> newFile();
    
    public Optional<Path> newFolder();
    
    public boolean delete(Path path);
    
    public Path rename(Path path);
    
    public Pair<Boolean, Path> move(Path source, Path dest);
    
    public String open(Path path);
}
