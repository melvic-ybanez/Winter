package winter.controllers.projects;

import javafx.scene.control.ContextMenu;
import winter.controllers.editors.EditorSetController;
import winter.factories.ProjectControllerBehaviors;
import winter.models.projects.ProjectModel;
import winter.views.project.ProjectNodeView;

/**
 * Created by ybamelcash on 6/23/2015.
 */
public class DirectoryProjectController extends ProjectController {
    public DirectoryProjectController(ProjectModel projectModel, EditorSetController editorSetController) {
        super(projectModel);
        setEditorSetController(editorSetController);
        setProjectNodeView(new ProjectNodeView(projectModel, this) {
            @Override
            public ContextMenu getMenu() {
                return createFolderContextMenu();
            }
        });
        setOpenBehavior(ProjectControllerBehaviors.doNothing());
        setDeleteBehavior(ProjectControllerBehaviors.deleteDirectory(getProjectModel().getPath()));
        setNewFileBehavior(ProjectControllerBehaviors.newFile(getProjectModel().getPath()));
        setNewDirectoryBehavior(ProjectControllerBehaviors.newDirectory(getProjectModel().getPath()));
    }
}
