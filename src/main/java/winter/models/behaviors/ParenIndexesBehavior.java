package winter.models.behaviors;

import winter.models.editors.EditorModel;
import winter.utils.Pair;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by ybamelcash on 7/17/2015.
 */
public interface ParenIndexesBehavior {
    public Function<Character, Optional<Pair<Integer, Integer>>> apply(EditorModel editorModel);
}
