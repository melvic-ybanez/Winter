package winter.models.behaviors;

import winter.models.editors.EditorModel;
import winter.utils.Pair;

import java.util.Optional;

/**
 * Created by ybamelcash on 7/17/2015.
 */
public interface ActiveParenIndexesBehavior {
    public Optional<Pair<Integer, Integer>> apply(EditorModel editorModel);
}
