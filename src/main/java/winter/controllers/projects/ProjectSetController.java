package winter.controllers.projects;

import winter.models.projects.ProjectModel;
import winter.views.project.ProjectSetView;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 7/18/2015.
 */
public interface ProjectSetController {
    public void openProject(Path path);

    public void closeProject(Path path);
    
    public ProjectSetView getProjectSetView();
}
