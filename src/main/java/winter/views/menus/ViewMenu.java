package winter.views.menus;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.*;
import org.fxmisc.richtext.LineNumberFactory;
import winter.controllers.editors.EditorSetController;
import winter.controllers.projects.ProjectSetController;
import winter.views.editor.EditorView;

/**
 * Created by ybamelcash on 7/5/2015.
 */
public class ViewMenu extends Menu {
    private EditorSetController editorSetController;
    private ProjectSetController projectSetController;
    private ToolBar toolBar;
    
    public ViewMenu(EditorSetController editorSetController, 
                    ProjectSetController projectSetController, 
                    ToolBar toolBar) {
        super("View");
        this.editorSetController = editorSetController;
        this.projectSetController = projectSetController;
        this.toolBar = toolBar;
        init();
    }
    
    private void init() {
        CheckMenuItem lineNumbersItem = new CheckMenuItem("Line Numbers");
        CheckMenuItem projectItem = new CheckMenuItem("Projects");
        CheckMenuItem toolBarItem = new CheckMenuItem("Toolbar");
        
        getItems().addAll(lineNumbersItem, new SeparatorMenuItem(),
                projectItem, toolBarItem);
        
        lineNumbersItem.setSelected(true);
        lineNumbersItem.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            editorSetController.getEditorSetView().getEditorControllers().forEach(controller -> {
                EditorView editorView = controller.getEditorView();
                if (isSelected) {
                    editorView.setParagraphGraphicFactory(LineNumberFactory.get(editorView));
                } else {
                    editorView.setParagraphGraphicFactory(null);
                } 
            });
        });
        
        toolBarItem.setSelected(true);
        toolBar.visibleProperty().bind(toolBarItem.selectedProperty());
        
        projectItem.setSelected(true);
        projectSetController.getProjectSetView().visibleProperty().bind(projectItem.selectedProperty());
    }
}
