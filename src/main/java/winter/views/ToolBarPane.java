package winter.views;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import winter.Resources;
import winter.factories.Icons;

/**
 * Created by ybamelcash on 7/8/2015.
 */
public class ToolBarPane extends ToolBar {
    public ToolBarPane() {
        super();
        getStyleClass().add("meruem-toolbar");
        Button newButton = Icons.createButtonIcon("new.png"); 
        Button openButton = Icons.createButtonIcon("open.png");
        Button saveButton = Icons.createButtonIcon("save.png");
        Button saveAsButton = Icons.createButtonIcon("save_as.png");
        
        Button undoButton = Icons.createButtonIcon("undo.png");
        Button redoButton = Icons.getRedoButton();
        
        Button copyButton = Icons.createButtonIcon("copy.png");
        Button cutButton = Icons.createButtonIcon("cut.png");
        Button pasteButton = Icons.createButtonIcon("paste.png");
        
        Button findButton = Icons.createButtonIcon("find.png");
        Button replaceButton = Icons.createButtonIcon("replace.png");
        
        Button runButton = Icons.createButtonIcon("run.png");
        Button replButton = Icons.createButtonIcon("repl.png");
        Button stopButton = Icons.createButtonIcon("stop.png");;
        
        Button preferencesButton = Icons.createButtonIcon("preferences.png");;
        
        Button helpButton = Icons.createButtonIcon("help.png");;
        
        getItems().addAll(newButton, openButton, saveButton, saveAsButton, new Separator(),
                undoButton, redoButton, new Separator(),
                copyButton, cutButton, pasteButton, new Separator(),
                findButton, replaceButton, new Separator(),
                runButton, replButton, stopButton, new Separator(),
                preferencesButton, helpButton); 
    }
}
