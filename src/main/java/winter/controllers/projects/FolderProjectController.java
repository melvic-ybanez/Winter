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
    public FolderProjectController(ProjectModel projectModel) {
        super(projectModel);
        setProjectNodeView(new ProjectNodeView(projectModel, this) {
            @Override
            public ContextMenu getMenu() {
                return createFolderContextMenu();
            }
        });
        setOpenBehavior(ProjectControllerBehaviors.doNothing());
        setDeleteBehavior(ProjectControllerBehaviors.deleteFolder(getProjectNodeView(), getProjectModel().getPath()));
    }
}
