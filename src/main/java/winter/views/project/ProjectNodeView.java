package winter.views.project;

import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import javafx.scene.control.*;
import winter.Resources;
import winter.controllers.projects.ProjectController;
import winter.models.projects.ProjectModel;

import java.nio.file.Path;
import java.util.Optional;

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
    
    public ProjectNodeView() { }

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
        MenuItem addFolderItem = createAddFolderItem();
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
        MenuItem addFolderItem = createAddFolderItem();
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
        return projectContextMenu;
    }
    
    private MenuItem createAddFileItem() {
        MenuItem newFileItem = new MenuItem("Add new File...", Resources.getIcon("new.png"));
        return newFileItem;
    }
    
    private MenuItem createAddFolderItem() {
        MenuItem newFolderItem = new MenuItem("Add new Folder...", Resources.getIcon("close_folder.png"));
        return newFolderItem;
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
    
    public static ProjectNodeView createDummy() {
        return new ProjectNodeView() {
            @Override
            public ContextMenu getMenu() {
                return null;
            }
        };
    }
}
