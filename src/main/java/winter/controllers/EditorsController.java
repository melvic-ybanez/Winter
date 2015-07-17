package winter.controllers;

import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;
import winter.models.EditorModel;
import winter.models.MeruemEditorModel;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Created by ybamelcash on 7/17/2015.
 */
public interface EditorsController {
    public EditorController getActiveEditorController();
    
    public CodeArea getActiveCodeArea();
    
    public boolean closeTab(Tab tab);
    
    public void closeOtherTabs();
    
    public boolean closeAllTabs();

    public Optional<EditorController> find(Path path);

    public boolean exists(Path path);
    
    public List<EditorController> remove(Path path);
}
