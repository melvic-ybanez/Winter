package winter.utils;

import java.io.IOException;

/**
 * Created by ybamelcash on 8/9/2015.
 */
public interface IOSupplier<T> {
    public T get() throws IOException;
    
    public static <T> Either<IOException, T> get(IOSupplier<T> supplier) {
        try {
            return Either.right(supplier.get());
        } catch (IOException ex) {
            return Either.left(ex);
        }
    } 
}
