package winter.controllers.projects;

import javafx.scene.control.ContextMenu;
import winter.controllers.editors.EditorSetController;
import winter.factories.ProjectControllerBehaviors;
import winter.models.projects.ProjectModel;
import winter.utils.*;
import winter.views.editor.EditorSetView;
import winter.views.project.ProjectNodeView;
import winter.views.project.ProjectSetView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 8/6/2015.
 */
public class FileProjectController extends ProjectController {
    private EditorSetController editorSetController;
    
    public FileProjectController(ProjectModel projectModel, EditorSetController editorSetController) {
        super(projectModel);
        setProjectNodeView(new ProjectNodeView(projectModel, this) {
            @Override
            public ContextMenu getMenu() {
                return createFileContextMenu();
            }
        });
        setOpenBehavior(ProjectControllerBehaviors.openTextFile(projectModel, editorSetController));
        setDeleteBehavior(ProjectControllerBehaviors.deleteFile(getProjectNodeView(), getProjectModel().getPath()));
    }
}
