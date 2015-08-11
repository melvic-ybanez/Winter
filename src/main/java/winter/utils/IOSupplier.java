package winter.utils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

/**
 * Created by ybamelcash on 8/9/2015.
 */
public interface IOSupplier<T> {
    public T get() throws IOException;
    
    public static <T> Either<IOException, T> get(IOSupplier<T> supplier) {
        try {
            return Either.right(supplier.get());
        } catch (FileAlreadyExistsException ex) {
            IOException ex1 = new FileAlreadyExistsException(ex.getFile(), ex.getOtherFile(), "File already exists.");
            return Either.left(ex1);
        } catch (IOException ex) {
            return Either.left(ex);
        }
    } 
}
