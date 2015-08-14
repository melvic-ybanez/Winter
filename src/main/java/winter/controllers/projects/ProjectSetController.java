package winter.controllers.projects;

import winter.views.project.ProjectSetView;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 7/18/2015.
 */
public interface ProjectSetController {
    public void openProject(Path path);

    public void openProject(Path path, int index);

    public void closeAll();

    public void refreshAll();
    
    public ProjectSetView getProjectSetView();
}
