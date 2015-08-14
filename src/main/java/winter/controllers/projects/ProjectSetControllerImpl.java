package winter.controllers.projects;

import javafx.beans.property.ReadOnlyDoubleProperty;
import winter.factories.ProjectControllerBehaviors;
import winter.views.editor.EditorSetView;
import winter.views.project.ProjectSetView;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public class ProjectSetControllerImpl implements ProjectSetController {
    private ProjectSetView projectSetView;
    private EditorSetView editorSetView;
    private Runnable closeAllBehavior;
    private Runnable refreshAllBehavior;

    public ProjectSetControllerImpl(EditorSetView editorSetView, ReadOnlyDoubleProperty heightProperty) {
        setProjectSetView(new ProjectSetView(this, editorSetView, heightProperty));
        closeAllBehavior = ProjectControllerBehaviors.removeAllProjects(this);
        refreshAllBehavior = ProjectControllerBehaviors.refreshAllProjects(this);
    }

    @Override
    public void openProject(Path path) {
        openProject(path, -1);
    }

    @Override
    public void openProject(Path path, int index) {
        projectSetView.openProject(path, index);
    }

    @Override
    public void closeAll() {
        closeAllBehavior.run();
    }

    @Override
    public void refreshAll() {
        refreshAllBehavior.run();
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
