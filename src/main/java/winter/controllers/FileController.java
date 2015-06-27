package winter.controllers;

import winter.Globals;
import winter.models.EditorModel;
import winter.utils.Either;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.charset.Charset;
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
    
    public static Either<IOException, Boolean> saveFile() {
        EditorModel activeEditor = EditorController.getActiveEditor();
        return activeEditor.getPath().map(path -> {
            return writeToFile(path, activeEditor.getContents())
                    .<Either<IOException, Boolean>>map(Either::left)
                    .orElseGet(() -> Either.right(true));
        }).orElseGet(() -> Either.right(false));
    }
    
    public static Either<IOException, Optional<String>> saveAsFile(Path path) {
        if (Files.exists(path)) {
            return Either.right(Optional.of("File already exists"));
        } else {
            EditorModel activeEditor = EditorController.getActiveEditor();
            return writeToFile(path, activeEditor.getContents())
                    .<Either<IOException, Optional<String>>>map(Either::left)
                    .orElseGet(() -> Either.right(Optional.empty()));
        }
    }
    
    private static Optional<IOException> writeToFile(Path path, String contents) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(contents);
            return Optional.empty();
        } catch (IOException ex) {
            return Optional.of(ex);
        }
    }
} 
