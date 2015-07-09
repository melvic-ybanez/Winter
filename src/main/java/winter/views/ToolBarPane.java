package winter.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import winter.Resources;
import winter.views.edit.FindPane;

/**
 * Created by ybamelcash on 7/8/2015.
 */
public class ToolBarPane extends ToolBar {
    public ToolBarPane() {
        super();
        getStyleClass().add("meruem-toolbar");
        Button newButton = new Button("", Resources.getIcon("new.png")); 
        Button openButton = new Button("", Resources.getIcon("open.png"));
        Button saveButton = new Button("", Resources.getIcon("save.png"));
        Button saveAsButton = new Button("", Resources.getIcon("save_as.png"));
        
        Button undoButton = new Button("", Resources.getIcon("undo.png"));
        Button redoButton = new Button("", Resources.getRedoIcon());
        
        Button copyButton = new Button("", Resources.getIcon("copy.png"));
        Button cutButton = new Button("", Resources.getIcon("cut.png"));
        Button pasteButton = new Button("", Resources.getIcon("paste.png"));
        
        Button findButton = new Button("", Resources.getIcon("find.png"));
        Button replaceButton = new Button("", Resources.getIcon("replace.png"));
        
        Button runButton = new Button("", Resources.getIcon("run.png"));
        Button replButton = new Button("", Resources.getIcon("repl.png"));
        Button stopButton = new Button("", Resources.getIcon("stop.png"));
        
        Button preferencesButton = new Button("", Resources.getIcon("preferences.png"));
        
        Button helpButton = new Button("", Resources.getIcon("help.png"));
        
        getItems().addAll(newButton, openButton, saveButton, saveAsButton, new Separator(),
                undoButton, redoButton, new Separator(),
                copyButton, cutButton, pasteButton, new Separator(),
                findButton, replaceButton, new Separator(),
                runButton, replButton, stopButton, new Separator(),
                preferencesButton, helpButton); 
    }
}
