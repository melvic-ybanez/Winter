package winter.controllers.projects;

import javafx.beans.property.ReadOnlyDoubleProperty;
import winter.views.editor.EditorSetView;
import winter.views.project.ProjectSetView;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public class ProjectSetControllerImpl implements ProjectSetController {
    private ProjectSetView projectSetView;
    private EditorSetView editorSetView;

    public ProjectSetControllerImpl(EditorSetView editorSetView, ReadOnlyDoubleProperty heightProperty) {
        setProjectSetView(new ProjectSetView(editorSetView, heightProperty));
    }

    @Override
    public void openProject(Path path) {
        projectSetView.openProject(path);
    }

    @Override
    public void refreshProject(Path path) {
        
    }

    public ProjectSetView getProjectSetView() {
        return projectSetView;
    }

    public void setProjectSetView(ProjectSetView projectSetView) {
        this.projectSetView = projectSetView;
    }

    public EditorSetView getEditorSetView() {
        return editorSetView;
    }

    public void setEditorSetView(EditorSetView editorSetView) {
        this.editorSetView = editorSetView;
    }
}
