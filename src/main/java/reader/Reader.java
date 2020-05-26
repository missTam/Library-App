package reader;

import javax.annotation.Nullable;
import java.util.List;

public interface Reader<T> {
    List<T> readData(@Nullable String name);
}
