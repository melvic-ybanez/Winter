package winter.controllers;

import winter.utils.Either;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public class FileController {
    public static Either<IOException, String> openFile(Path path) {
        String contents = "";
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset())) {
            while (true) {
                Optional<String> lineOpt = Optional.ofNullable(reader.readLine());
                if (lineOpt.isPresent()) {
                    contents += lineOpt.get() + "\n";
                } else {
                    if (!contents.isEmpty()) {
                        // remove the extra newline character
                        contents = contents.substring(0, contents.length() - 1);
                    }
                    break;
                }
            }
            return Either.right(contents);
        } catch (IOException ex) {
            return Either.left(ex);
        }
    }
}
