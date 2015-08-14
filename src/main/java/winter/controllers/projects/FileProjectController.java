package winter.controllers.projects;

import javafx.scene.control.ContextMenu;
import winter.controllers.editors.EditorSetController;
import winter.factories.ProjectControllerBehaviors;
import winter.models.projects.ProjectModel;
import winter.views.project.ProjectNodeView;

/**
 * Created by ybamelcash on 8/6/2015.
 */
public class FileProjectController extends ProjectController {
    public FileProjectController(ProjectModel projectModel,
                                 ProjectSetController projectSetController, EditorSetController editorSetController) {
        super(projectModel, projectSetController);
        setEditorSetController(editorSetController);
        setProjectNodeView(new ProjectNodeView(projectModel, this) {
            @Override
            public ContextMenu getMenu() {
                return createFileContextMenu();
            }
        });
        setOpenBehavior(ProjectControllerBehaviors.openTextFile(projectModel, editorSetController));
        setDeleteBehavior(ProjectControllerBehaviors.deleteFile(getProjectModel().getPath()));
        setNewFileBehavior(ProjectControllerBehaviors.doNothing());
        setNewDirectoryBehavior(ProjectControllerBehaviors.doNothing());
        setCloseBehavior(ProjectControllerBehaviors.acceptNothing());
        setRefreshBehavior(ProjectControllerBehaviors.doNothing());
        setRenameBehavior(ProjectControllerBehaviors.renameFile(getProjectModel().getPath()));
    }
}
