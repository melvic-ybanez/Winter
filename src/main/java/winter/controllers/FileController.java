package winter.controllers;

import winter.utils.Either;
import winter.utils.Errors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public class FileController {
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
        Optional<IOException> errorOpt = writeToFile(path, contents);
        return errorOpt;
    }
    
    private static Optional<IOException> writeToFile(Path path, String contents) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(contents);
            return Optional.empty();
        } catch (IOException ex) {
            return Optional.of(ex);
        }
    }
    
    public static Either<IOException, Either<String, Path>> renameFile(Path path, String newName) {
        Path newPath = path.resolveSibling(newName);
        if (Files.exists(newPath)) {
            return Either.right(Either.left(Errors.messages.fileAlreadyExists(newPath)));
        } else {
            try {
                newPath = Files.move(path, newPath);
                return Either.right(Either.<String, Path>right(newPath));
            } catch (IOException ex) {
                return Either.left(ex);
            }
        }
    } 
} 
