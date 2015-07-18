package winter.controllers;

import javafx.beans.property.ReadOnlyDoubleProperty;
import winter.views.editors.EditorSetView;
import winter.views.projects.ProjectSetView;

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
    public void displayProject(Path path) {
        projectSetView.displayProject(path);
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
