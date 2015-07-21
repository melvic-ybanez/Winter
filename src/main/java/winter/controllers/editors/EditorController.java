package winter.controllers.editors;

import javafx.scene.input.KeyEvent;
import winter.controllers.file.FileController;
import winter.models.editor.EditorModel;
import winter.views.editor.EditorView;

/**
 * Created by ybamelcash on 7/17/2015.
 */
public interface EditorController {
    public void rename();
    
    public void editorAreaChanged(String newText);
    
    public void updateTabGraphic();
    
    public void undo();
    
    public void redo();
    
    public void copy();
    
    public void cut();
    
    public void paste();
    
    public boolean runAccelerators(KeyEvent event);
    
    public boolean whenHasChanges(Runnable function);
    
    public void autoIndent();
    
    public EditorView getEditorView();
    
    public EditorModel getEditorModel();

    public void setFileController(FileController fileController);

    public FileController getFileController();
}
