package winter.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 7/18/2015.
 */
public class FileUtils {
    public static Either<IOException, String> openFile(Path path) {
        StringBuilder contents = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            while (true) {
                Optional<String> lineOpt = Optional.ofNullable(reader.readLine());
                if (lineOpt.isPresent()) {
                    contents.append(lineOpt.get()).append("\n");
                } else {
                    if (contents.length() > 0) {
                        // remove the extra newline character
                        contents = new StringBuilder(contents.substring(0, contents.length() - 1));
                    }
                    break;
                }
            }
            return Either.right(contents.toString());
        } catch (IOException ex) {
            return Either.left(ex);
        }
    }

    public static Either<IOException, Boolean> saveFile(Optional<Path> pathOpt, String contents) {
        return pathOpt.map(path -> {
            return saveAsFile(path, contents)
                    .<Either<IOException, Boolean>>map(Either::left)
                    .orElseGet(() -> Either.right(true));
        }).orElseGet(() -> Either.right(false));
    }

    public static Optional<IOException> saveAsFile(Path path, String contents) {
        return writeToFile(path, contents);
    }
    
    public static Optional<IOException> writeToFile(Path path, String contents) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(contents);
            return Optional.empty();
        } catch (IOException ex) {
            return Optional.of(ex);
        }
    }

    public static Either<IOException, Path> renameFile(Path path, String newName) {
        Path newPath = path.resolveSibling(newName);
        return moveFile(path, newPath);
    }
    
    public static Either<IOException, Path> moveFile(Path source, Path dest) {
        return IOSupplier.get(() -> Files.move(source, dest));
    }
    
    public static Either<IOException, Path> createFile(Path path) {
        return IOSupplier.get(() -> Files.createFile(path));
    }
    
    public static Either<IOException, Path> createDirectory(Path path) {
        return IOSupplier.get(() -> Files.createDirectory(path));
    }
    
    public static Optional<IOException> deleteFile(Path path) {
        return IORunnable.run(() -> Files.delete(path));
    }
    
    public static Optional<IOException> deleteDirectory(Path path) {
        return IORunnable.run(() -> org.apache.commons.io.FileUtils.deleteDirectory(path.toFile()));
    }
}
