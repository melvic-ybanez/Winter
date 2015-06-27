package winter.controllers;

import winter.Globals;
import winter.models.EditorModel;
import winter.utils.StreamUtils;
import winter.views.editors.EditorPane;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class EditorController {
    public static EditorModel getActiveEditor() {
        EditorPane editorPane = Globals.editorPane;
        int activeIndex = editorPane.getTabPane().getSelectionModel().getSelectedIndex();
        return editorPane.getEditors().get(activeIndex);
    }
    
    public static Optional<EditorModel> find(List<EditorModel> editors, Path path) {
        return StreamUtils.find(editors.stream(), model -> model.equalsPath(path));
    }
    
    public static boolean exists(List<EditorModel> editors, Path path) {
        return StreamUtils.exists(editors.stream(), model -> model.equalsPath(path));
    }
    
    public static List<EditorModel> remove(List<EditorModel> editors, Path path) {
        return StreamUtils.remove(editors.stream(), model -> model.equalsPath(path)).collect(Collectors.toList());
    }
}
