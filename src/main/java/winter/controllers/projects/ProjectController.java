package winter.controllers.projects;

import javafx.concurrent.Task;
import winter.controllers.editors.EditorSetController;
import winter.models.projects.ProjectModel;
import winter.utils.Errors;
import winter.utils.WatchableDir;
import winter.views.project.ProjectNodeView;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by ybamelcash on 7/16/2015.
 */
public class ProjectController implements WatchableDir {
    private Consumer<ProjectNodeView> newFileBehavior;
    private Consumer<ProjectNodeView> newDirectoryBehavior;
    private Consumer<ProjectNodeView> deleteBehavior;
    private Consumer<String> renameBehavior;
    private Consumer<Path> moveBehavior;
    private Runnable openBehavior;
    private Consumer<ProjectNodeView> closeBehavior;
    private Consumer<ProjectNodeView> refreshBehavior;
    
    private ProjectModel projectModel;
    private ProjectNodeView projectNodeView;
    private EditorSetController editorSetController;
    private ProjectSetController projectSetController;

    private WatchService watcher;
    private Map<WatchKey, Path> keyMap;

    public ProjectController(ProjectModel projectModel, ProjectSetController projectSetController) {
        setProjectModel(projectModel);
        this.projectSetController = projectSetController;

        try {
            watcher = FileSystems.getDefault().newWatchService();
            keyMap = new HashMap<>();
        } catch (IOException e) {
            showRegisterErrorDialogAndExit(e);
        }
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

    public void close() {
        closeBehavior.accept(getProjectNodeView());
    }

    public void refresh() {
        refreshBehavior.accept(getProjectNodeView());
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

    public Consumer<ProjectNodeView> getCloseBehavior() {
        return closeBehavior;
    }

    public void setCloseBehavior(Consumer<ProjectNodeView> closeBehavior) {
        this.closeBehavior = closeBehavior;
    }

    public Consumer<ProjectNodeView> getRefreshBehavior() {
        return refreshBehavior;
    }

    public void setRefreshBehavior(Consumer<ProjectNodeView> refreshBehavior) {
        this.refreshBehavior = refreshBehavior;
    }

    @Override
    public void register(Path dir) {
        try {
            WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            keyMap.put(key, dir);
        } catch (IOException e) {
            showRegisterErrorDialogAndExit(e);
        }
    }

    @Override
    public void processEvents() {
        for (;;) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException ex) {
                return;
            }

            Path dir = keyMap.get(key);
            if (dir == null) continue;
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                if (kind == OVERFLOW) continue;

                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;

                Path name = ev.context();
                Path child = dir.resolve(name);

                if (kind == ENTRY_CREATE) {
                    if (Files.isDirectory(child)) {
                        projectNodeView.addDirectory(child);
                        register(child);
                    } else {
                        projectNodeView.addNewFile(child);
                    }
                } else if (kind == ENTRY_DELETE) {
                    projectNodeView.getChildren().removeIf((treeItem) -> {
                        ProjectNodeView projectNodeView1 = (ProjectNodeView) treeItem;
                        return projectNodeView1.getProjectModel().getPath().equals(child);
                    });
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                keyMap.remove(key);
                if (keyMap.isEmpty()) break;
            }
        }
    }

    @Override
    public void start() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                register(getProjectModel().getPath());
                processEvents();
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void showRegisterErrorDialogAndExit(IOException ex) {
        Errors.registerWatcherException(ex);
        System.exit(-1);
    }

    public ProjectSetController getProjectSetController() {
        return projectSetController;
    }
}
