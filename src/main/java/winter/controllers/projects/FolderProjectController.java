package winter.controllers.projects;

import javafx.scene.control.ContextMenu;
import winter.factories.ProjectControllerBehaviors;
import winter.models.projects.ProjectModel;
import winter.views.project.ProjectNodeView;
import winter.views.project.ProjectSetView;

/**
 * Created by ybamelcash on 6/23/2015.
 */
public class FolderProjectController extends ProjectController {
    private ProjectSetView projectSetView;
    
    public FolderProjectController(ProjectModel projectModel, ProjectSetView projectSetView) {
        super(projectModel);
        this.projectSetView = projectSetView;
        setProjectNodeView(new ProjectNodeView(projectModel, this) {
            @Override
            public ContextMenu getMenu() {
                return projectSetView.createFolderContextMenu();
            }
        });
        setOpenBehavior(ProjectControllerBehaviors.doNothing());
    }
}
