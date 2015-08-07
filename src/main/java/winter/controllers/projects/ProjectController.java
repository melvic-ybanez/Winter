package winter.controllers.projects;

import winter.models.projects.ProjectModel;
import winter.utils.Either;
import winter.utils.Observable;
import winter.utils.Pair;
import winter.views.project.ProjectNodeView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by ybamelcash on 7/16/2015.
 */
public class ProjectController {
    private Runnable newFileBehavior;
    private Runnable newFolderBehavior;
    private Runnable deleteBehavior;
    private Consumer<String> renameBehavior;
    private Consumer<Path> moveBehavior;
    private Runnable openBehavior;
    private Runnable refreshBehavior;
    private Runnable closeBehavior;
    
    private ProjectModel projectModel;
    private ProjectNodeView projectNodeView;
    
    public ProjectController(ProjectModel projectModel) {
        setProjectModel(projectModel);
    }
    
    public void newFile() {
        newFileBehavior.run();
    }
    
    public void newFolder() {
        newFolderBehavior.run();
    }
    
    public void delete() {
        deleteBehavior.run();
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
    
    public void close() {
        closeBehavior.run();
    }
    
    public void refresh() {
        refreshBehavior.run();
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

    public Runnable getNewFileBehavior() {
        return newFileBehavior;
    }

    public void setNewFileBehavior(Runnable newFileBehavior) {
        this.newFileBehavior = newFileBehavior;
    }

    public Runnable getNewFolderBehavior() {
        return newFolderBehavior;
    }

    public void setNewFolderBehavior(Runnable newFolderBehavior) {
        this.newFolderBehavior = newFolderBehavior;
    }

    public Runnable getDeleteBehavior() {
        return deleteBehavior;
    }

    public void setDeleteBehavior(Runnable deleteBehavior) {
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
}
