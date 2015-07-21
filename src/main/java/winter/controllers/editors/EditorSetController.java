package winter.controllers.editors;

import javafx.scene.control.Tab;
import winter.controllers.files.FileController;
import winter.views.editor.EditorSetView;
import winter.views.editor.EditorView;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Created by ybamelcash on 7/17/2015.
 */
public interface EditorSetController {
    public EditorController getActiveEditorController();
    
    public EditorView getActiveEditorView();
    
    public boolean closeTab(Tab tab);
    
    public void closeOtherTabs();
    
    public boolean closeAllTabs();

    public Optional<EditorController> find(Path path);

    public boolean exists(Path path);
    
    public List<EditorController> remove(Path path);
    
    public void setEditorSetView(EditorSetView editorSetView);
    
    public EditorSetView getEditorSetView();
    
    public void setFileController(FileController fileController);
    
    public FileController getFileController();
}
