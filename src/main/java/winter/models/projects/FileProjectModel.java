package winter.models.projects;

import winter.utils.Pair;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 8/6/2015.
 */
public class FileProjectModel implements ProjectModel {
    @Override
    public Optional<Path> newFile() {
        throw new UnsupportedOperationException("Can't create a file within a file");
    }

    @Override
    public Optional<Path> newFolder() {
        throw new UnsupportedOperationException("Can't create a folder within a file");
    }

    @Override
    public boolean delete(Path path) {
        return false;
    }

    @Override
    public Path rename(Path path) {
        return null;
    }

    @Override
    public Pair<Boolean, Path> move(Path source, Path dest) {
        return null;
    }

    @Override
    public String open(Path path) {
        return null;
    }
}
