package winter.views.project;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import winter.controllers.projects.ProjectController;
import winter.models.projects.ProjectModel;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public abstract class ProjectNodeView extends TreeItem<String> {
    private ProjectModel projectModel;
    private ProjectController projectController;
    
    public ProjectNodeView(ProjectModel projectModel, ProjectController projectController) {
        super(projectModel.getName());
        setProjectController(projectController);
        setProjectModel(projectModel);
    }
    
    public ProjectNodeView() { }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    public ProjectController getProjectController() {
        return projectController;
    }

    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
    }
    
    public abstract ContextMenu getMenu();
    
    public static ProjectNodeView createDummy() {
        return new ProjectNodeView() {
            @Override
            public ContextMenu getMenu() {
                return null;
            }
        };
    }
}
