package winter.controllers;

import winter.views.menus.FileMenu;

/**
 * Created by ybamelcash on 7/19/2015.
 */
public interface FileController {
    public void openFile();
    
    public void saveFile();
    
    public void saveAsFile();
    
    public FileMenu getFileMenu();
}
