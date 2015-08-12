package winter.controllers.projects;

import winter.controllers.editors.EditorSetController;
import winter.models.projects.ProjectModel;
import winter.views.project.ProjectNodeView;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Created by ybamelcash on 7/16/2015.
 */
public class ProjectController {
    private Consumer<ProjectNodeView> newFileBehavior;
    private Consumer<ProjectNodeView> newDirectoryBehavior;
    private Consumer<ProjectNodeView> deleteBehavior;
    private Consumer<String> renameBehavior;
    private Consumer<Path> moveBehavior;
    private Runnable openBehavior;
    
    private ProjectModel projectModel;
    private ProjectNodeView projectNodeView;
    private EditorSetController editorSetController;
    
    public ProjectController(ProjectModel projectModel) {
        setProjectModel(projectModel);
    }
    
    public void newFile() {
        newFileBehavior.accept(getProjectNodeView());
    }
    
    public void newDirectory() {
        newDirectoryBehavior.accept(getProjectNodeView());
    }
    
    public void delete() {
        deleteBehavior.accept(getProjectNodeView());
    }
    
    public void rename(String newName) {
        renameBehavior.accept(newName);
    }
    
    public void move(Path dest) {
        moveBehavior.accept(dest);
    }
    
    public void open() {
        openBehavior.run();
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    public ProjectNodeView getProjectNodeView() {
        return projectNodeView;
    }

    public void setProjectNodeView(ProjectNodeView projectNodeView) {
        this.projectNodeView = projectNodeView;
    }

    public Consumer<ProjectNodeView> getNewFileBehavior() {
        return newFileBehavior;
    }

    public void setNewFileBehavior(Consumer<ProjectNodeView> newFileBehavior) {
        this.newFileBehavior = newFileBehavior;
    }

    public Consumer<ProjectNodeView> getNewDirectoryBehavior() {
        return newDirectoryBehavior;
    }

    public void setNewDirectoryBehavior(Consumer<ProjectNodeView> newDirectoryBehavior) {
        this.newDirectoryBehavior = newDirectoryBehavior;
    }

    public Consumer<ProjectNodeView> getDeleteBehavior() {
        return deleteBehavior;
    }

    public void setDeleteBehavior(Consumer<ProjectNodeView> deleteBehavior) {
        this.deleteBehavior = deleteBehavior;
    }

    public Consumer<String> getRenameBehavior() {
        return renameBehavior;
    }

    public void setRenameBehavior(Consumer<String> renameBehavior) {
        this.renameBehavior = renameBehavior;
    }

    public Consumer<Path> getMoveBehavior() {
        return moveBehavior;
    }

    public void setMoveBehavior(Consumer<Path> moveBehavior) {
        this.moveBehavior = moveBehavior;
    }

    public Runnable getOpenBehavior() {
        return openBehavior;
    }

    public void setOpenBehavior(Runnable openBehavior) {
        this.openBehavior = openBehavior;
    }

    public EditorSetController getEditorSetController() {
        return editorSetController;
    }

    public void setEditorSetController(EditorSetController editorSetController) {
        this.editorSetController = editorSetController;
    }
}
