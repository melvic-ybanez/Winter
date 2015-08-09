package winter.views.menus;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import winter.controllers.editors.EditorController;
import winter.controllers.editors.EditorSetController;
import winter.controllers.projects.ProjectSetController;
import winter.utils.Observer;
import winter.views.editor.EditorView;
import winter.views.editor.LineNumberView;

/**
 * Created by ybamelcash on 7/5/2015.
 */
public class ViewMenu extends Menu implements Observer {
    private EditorSetController editorSetController;
    private ProjectSetController projectSetController;
    private ToolBar toolBar;

    private CheckMenuItem lineNumbersItem = new CheckMenuItem("Line Numbers");
    private CheckMenuItem projectsItem = new CheckMenuItem("Projects");
    
    public ViewMenu(EditorSetController editorSetController, 
                    ProjectSetController projectSetController, 
                    ToolBar toolBar) {
        super("View");
        this.editorSetController = editorSetController;
        this.projectSetController = projectSetController;
        this.toolBar = toolBar;
        init();
        editorSetController.getObservable().registerObserver(this);
    }
    
    private void init() {
        CheckMenuItem toolBarItem = new CheckMenuItem("Toolbar");
        
        getItems().addAll(lineNumbersItem, new SeparatorMenuItem(),
                projectsItem, toolBarItem);
        
        lineNumbersItem.setSelected(true);
        lineNumbersItem.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            editorSetController.getEditorSetView().getEditorControllers().forEach(controller -> {
                toggleLineNumber(controller, isSelected);
            });
        });
        
        toolBarItem.setSelected(true);
        toolBar.visibleProperty().bind(toolBarItem.selectedProperty());
        
        projectsItem.setSelected(true);
        projectSetController.getProjectSetView().visibleProperty().bind(projectsItem.selectedProperty());
    }
    
    private void toggleLineNumber(EditorController editorController, boolean isSelected) {
        EditorView editorView = editorController.getEditorView();
        if (isSelected) {
            editorView.setParagraphGraphicFactory(new LineNumberView(editorView));
        } else {
            editorView.setParagraphGraphicFactory(null);
        }
    }

    @Override
    public void update() {
        toggleLineNumber(editorSetController.getActiveEditorController(), lineNumbersItem.isSelected());
    }

    public CheckMenuItem getLineNumbersItem() {
        return lineNumbersItem;
    }

    public CheckMenuItem getProjectsItem() {
        return projectsItem;
    }
}
