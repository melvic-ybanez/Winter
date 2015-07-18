package winter.controllers;

import javafx.scene.input.KeyEvent;
import winter.models.EditorModel;
import winter.models.MeruemEditorModel;
import winter.views.editors.EditorView;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 7/17/2015.
 */
public interface EditorController {
    public void rename(Path newPath);
    
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
    
    public void saveFile();
    
    public void saveAsFile();
}
