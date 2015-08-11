package winter.controllers.projects;

import javafx.scene.control.ContextMenu;
import winter.controllers.editors.EditorSetController;
import winter.factories.ProjectControllerBehaviors;
import winter.models.projects.ProjectModel;
import winter.views.project.ProjectNodeView;

/**
 * Created by ybamelcash on 8/8/2015.
 */
public class ProjectProjectController extends DirectoryProjectController {
    public ProjectProjectController(ProjectModel projectModel, EditorSetController editorSetController) {
        super(projectModel, editorSetController);
        setProjectNodeView(new ProjectNodeView(projectModel, this) {
            @Override
            public ContextMenu getMenu() {
                return createProjectContextMenu();
            }
        });
        setCloseBehavior(ProjectControllerBehaviors.removeFromProject());
    }
}
