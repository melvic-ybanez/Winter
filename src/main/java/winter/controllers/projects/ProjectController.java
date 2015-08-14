package winter.controllers.projects;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import winter.controllers.editors.EditorSetController;
import winter.models.projects.ProjectModel;
import winter.utils.Errors;
import winter.utils.WatchableDir;
import winter.views.project.ProjectNodeView;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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
    private Runnable refreshBehavior;
    
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

    public void insertNode(ProjectNodeView newNode) {
        Function<ProjectController, Boolean> isFile = controller -> controller instanceof FileProjectController;
        Function<ProjectController, Boolean> isDir = controller -> controller instanceof DirectoryProjectController;

        ProjectController controller1 = newNode.getProjectController();
        ObservableList<TreeItem<String>> children = projectNodeView.getChildren();
        int index = children.size();

        for (int i = 0; i < children.size(); i++) {
            ProjectNodeView childView = (ProjectNodeView) children.get(i);
            ProjectController controller2 = childView.getProjectController();

            // If the new node is a file and is matched against a directory, then we
            // should continue the loop instead of inserting the node here, since directories have
            // higher priorities than files.
            if (isFile.apply(controller1) && isDir.apply(controller2)) continue;

            // If the new node is a directory and is matched against a file, then insert it immediately
            // and break out of the loop.
            if (isDir.apply(controller1) && isFile.apply(controller2)) {
                index = i;
                break;
            }

            // If the two types are equal, compare their names alphabetically.
            String name1 = controller1.getProjectModel().getName();
            String name2 = controller2.getProjectModel().getName();
            int comparison = name1.compareTo(name2);
            if (comparison <= 0) {
                index = i;
                break;
            }
        }

        children.add(index, newNode);
        children.forEach(child -> {
            ProjectNodeView view = (ProjectNodeView) child;
            System.out.print(view.getProjectModel().getName() + " ");
        });
        System.out.println();
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

    public Runnable getRefreshBehavior() {
        return refreshBehavior;
    }

    public void setRefreshBehavior(Runnable refreshBehavior) {
        this.refreshBehavior = refreshBehavior;
    }
}
