package winter.views.project;

import javafx.scene.control.Label;
import winter.controllers.projects.ProjectController;
import winter.models.projects.ProjectModel;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public class ProjectNodeView extends Label {
    private ProjectModel projectModel;
    private ProjectController projectController;
    
    public ProjectNodeView(ProjectModel projectModel, ProjectController projectController) {
        setProjectController(projectController);
        setProjectModel(projectModel);
        setText(projectModel.getName());
    }

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
}
