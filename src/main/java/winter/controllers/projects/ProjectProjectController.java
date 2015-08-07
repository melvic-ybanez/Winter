package winter.controllers.projects;

import javafx.scene.control.ContextMenu;
import winter.models.projects.ProjectModel;
import winter.views.project.ProjectNodeView;

/**
 * Created by ybamelcash on 8/8/2015.
 */
public class ProjectProjectController extends ProjectController {
    public ProjectProjectController(ProjectModel projectModel) {
        super(projectModel);
        setProjectNodeView(new ProjectNodeView(projectModel, this) {
            @Override
            public ContextMenu getMenu() {
                return createProjectContextMenu();
            }
        });
    }
}
