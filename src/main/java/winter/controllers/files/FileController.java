package winter.controllers.files;

import winter.models.editors.EditorModel;
import winter.views.menus.FileMenu;

/**
 * Created by ybamelcash on 7/19/2015.
 */
public interface FileController {
    public void openFile();

    public boolean saveFile();

    public boolean saveFileAs();

    public boolean saveFile(EditorModel editorModel);

    public boolean saveFileAs(EditorModel editorModel);
    
    public FileMenu getFileMenu();
}
