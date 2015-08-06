package winter.controllers.projects;

import winter.factories.ProjectControllerBehaviors;
import winter.models.projects.ProjectModel;

/**
 * Created by ybamelcash on 6/23/2015.
 */
public class FolderProjectController extends ProjectController {
    public FolderProjectController(ProjectModel projectModel) {
        super(projectModel);
        setOpenBehavior(ProjectControllerBehaviors.doNothing());
    }
}
