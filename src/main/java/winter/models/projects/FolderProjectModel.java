package winter.models.projects;

import winter.utils.Pair;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/23/2015.
 */
public class FolderProjectModel implements ProjectModel {

    @Override
    public Optional<Path> newFile() {
        return null;
    }

    @Override
    public Optional<Path> newFolder() {
        return null;
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
        throw new UnsupportedOperationException("Unable to view folder on a text editor");
    }
}
