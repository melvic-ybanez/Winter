package winter.views.project;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import winter.Resources;
import winter.controllers.editors.EditorSetController;
import winter.controllers.projects.*;
import winter.models.projects.ProjectModel;
import winter.models.projects.ProjectModelImpl;
import winter.utils.Errors;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.Function;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public abstract class ProjectNodeView extends TreeItem<String> {
    private ProjectModel projectModel;
    private ProjectController projectController;
    
    public ProjectNodeView(ProjectModel projectModel, ProjectController projectController) {
        super(projectModel.getName());
        setProjectController(projectController);
        setProjectModel(projectModel);
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    public ProjectController getProjectController() {
        return projectController;
    }

    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
    }

    public ContextMenu createFileContextMenu() {
        ContextMenu fileContextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit", Resources.getIcon("open_file.png"));
        MenuItem deleteItem = createDeleteItem();
        MenuItem renameItem = createRenameItem();
        MenuItem moveItem = createMoveItem();

        fileContextMenu.getItems().addAll(editItem, deleteItem, renameItem, moveItem);
        
        editItem.setOnAction(e -> projectController.open());
        
        return fileContextMenu;
    }

    public ContextMenu createFolderContextMenu() {
        ContextMenu folderContextMenu = new ContextMenu();
        MenuItem addFileItem = createAddFileItem();
        MenuItem addFolderItem = createAddDirectoryItem();
        MenuItem deleteItem = createDeleteItem();
        MenuItem renameItem = createRenameItem();
        MenuItem moveItem = createMoveItem();

        folderContextMenu.getItems().addAll(addFileItem,
                addFolderItem, new SeparatorMenuItem(),
                deleteItem, renameItem, moveItem);
        return folderContextMenu;
    }
    
    public ContextMenu createProjectContextMenu() {
        ContextMenu projectContextMenu = new ContextMenu();
        MenuItem addFileItem = createAddFileItem();
        MenuItem addFolderItem = createAddDirectoryItem();
        MenuItem deleteItem = createDeleteItem();
        MenuItem renameItem = createRenameItem();
        MenuItem moveItem = createMoveItem();
        MenuItem refreshItem = new MenuItem("Refresh", Resources.getIcon("refresh.png"));
        MenuItem closeItem = new MenuItem("Close", Resources.getIcon("close.png"));
        MenuItem closeAllItems = new MenuItem("Close All");
                
        projectContextMenu.getItems().addAll(addFileItem,
                addFolderItem, new SeparatorMenuItem(),
                deleteItem, renameItem, moveItem, new SeparatorMenuItem(),
                refreshItem, closeItem, closeAllItems);

        closeItem.setOnAction(e -> projectController.close());
        refreshItem.setOnAction(e -> projectController.refresh());

        return projectContextMenu;
    }
    
    public ProjectNodeView addNewFile(Path path) {
        ProjectModel fileProjectModel = new ProjectModelImpl(path);
        ProjectController fileProjectController = new FileProjectController(fileProjectModel,
                projectController.getProjectSetController(),
                projectController.getEditorSetController());
        ProjectNodeView fileNode = fileProjectController.getProjectNodeView();
        fileNode.setGraphic(Resources.getIcon("file.png"));
        fileProjectController.start();
        projectController.insertNode(fileNode);
        return fileNode;
    }

    public ProjectNodeView addDirectory(Path dirPath, boolean isProject, int index) {
        ProjectModel dirProjectModel = new ProjectModelImpl(dirPath);
        ProjectController dirProjectController = isProject
                ? new ProjectProjectController(dirProjectModel,
                    projectController.getProjectSetController(),
                    projectController.getEditorSetController())
                : new DirectoryProjectController(dirProjectModel,
                    projectController.getProjectSetController(),
                    projectController.getEditorSetController());
        ProjectNodeView dirNode = dirProjectController.getProjectNodeView();

        dirNode.setGraphic(Resources.getIcon("close_folder.png"));
        dirNode.graphicProperty().bind(
                Bindings.when(dirNode.expandedProperty())
                        .then(Resources.getIcon("open_folder.png"))
                        .otherwise(Resources.getIcon("close_folder.png")));

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    dirNode.addDirectory(path);
                } else {
                    dirNode.addNewFile(path);
                }
            }
        } catch (IOException ex) {
            Errors.exceptionDialog("Add Folder Exception", "Unable to add folder: " + dirPath, ex.getMessage(), ex);
        }

        if (index == -1) {
            projectController.insertNode(dirNode);
        } else {
            getChildren().add(index, dirNode);
        }

        dirProjectController.start();
        return dirNode;
    }

    public ProjectNodeView addDirectory(Path dirPath, boolean isProject) {
        return addDirectory(dirPath, isProject, -1);
    }

    public ProjectNodeView addDirectory(Path dirPath) {
        return addDirectory(dirPath, false);
    }
    
    private MenuItem createAddFileItem() {
        MenuItem newFileItem = new MenuItem("Add new File...", Resources.getIcon("new.png"));
        newFileItem.setOnAction(e -> projectController.newFile());
        return newFileItem;
    }
    
    private MenuItem createAddDirectoryItem() {
        MenuItem newDirectoryItem = new MenuItem("Add new Directory...", Resources.getIcon("close_folder.png"));
        newDirectoryItem.setOnAction(e -> projectController.newDirectory());
        return newDirectoryItem;
    }
    
    private MenuItem createDeleteItem() {
        MenuItem deleteItem = new MenuItem("Delete", Resources.getIcon("delete.png"));
        deleteItem.setOnAction(e -> projectController.delete());
        return deleteItem;
    }
    
    private MenuItem createRenameItem() {
        MenuItem renameItem = new MenuItem("Rename...", Resources.getIcon("rename.png"));
        return renameItem;
    }
    
    private MenuItem createMoveItem() {
        MenuItem moveItem = new MenuItem("Move...", Resources.getIcon("move.png"));
        return moveItem;
    }
    
    public abstract ContextMenu getMenu();
    
    public static ProjectNodeView createDummy(ProjectSetController projectSetController, EditorSetController editorSetController) {
        ProjectModel projectModel = new ProjectModelImpl(Paths.get(""));
        ProjectController projectController = new DirectoryProjectController(projectModel,
                projectSetController, editorSetController);
        projectController.setProjectNodeView(new ProjectNodeView(projectModel, projectController) {
            @Override
            public ContextMenu getMenu() {
                return null;
            }
        });
        return projectController.getProjectNodeView();
    }
}
