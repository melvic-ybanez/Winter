package winter.views.project;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import winter.Resources;
import winter.controllers.projects.FileProjectController;
import winter.controllers.projects.FolderProjectController;
import winter.controllers.projects.ProjectController;
import winter.controllers.projects.ProjectProjectController;
import winter.models.projects.ProjectModel;
import winter.models.projects.ProjectModelImpl;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.FileUtils;
import winter.views.editor.EditorSetView;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ProjectSetView extends TitledPane {
    private TreeView<String> tree = new TreeView<>();
    private EditorSetView editorSetView;
    private ContextMenu folderContextMenu;
    private ContextMenu fileContextMenu;
    
    public ProjectSetView(EditorSetView editorSetView, ReadOnlyDoubleProperty heightProperty) {
        this.editorSetView = editorSetView;
        setText("Projects");
        setGraphic(Resources.getIcon("view_projects.png"));
        setContent(tree);
        prefHeightProperty().bind(heightProperty); 
        setCollapsible(false);
        
        tree.setRoot(ProjectNodeView.createDummy());
        tree.setShowRoot(false);
        tree.setCellFactory(treeView -> {
            return new TreeCell<String>() {
                @Override
                public void updateItem(String item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    if (isEmpty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(getItem() == null ? "" : getItem());
                        setGraphic(getTreeItem().getGraphic());
                        setContextMenu(((ProjectNodeView) getTreeItem()).getMenu());
                    }
                } 
            };
        });
        registerEvents();
        
        managedProperty().bind(visibleProperty());
    }
    
    private void registerEvents() {
        tree.setOnMouseClicked(event -> {
            ProjectNodeView item = (ProjectNodeView) tree.getSelectionModel().getSelectedItem();
            if (item == null) return;
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                Path path = item.getProjectModel().getPath();
                if (!Files.isDirectory(path)) {
                    Either<IOException, String> result = FileUtils.openFile(path);
                    result.getLeft().ifPresent(Errors::openFileExceptionDialog);
                    result.getRight().ifPresent(contents -> {
                        String filename = path.getFileName().toString();
                        TabPane tabPane = editorSetView.getTabPane();
                        Optional<Tab> existingTab = tabPane.getTabs()
                                .stream()
                                .filter(tab -> tab.getText().equals(filename))
                                .findFirst();
                        existingTab.ifPresent(tab -> tabPane.getSelectionModel().select(tab));
                        if (!existingTab.isPresent()) {
                            editorSetView.newEditorAreaTab(path, contents);
                        }
                    });
                }
            }
        });
    }
    
    public void displayProject(Path projectPath) {
        ProjectNodeView root = (ProjectNodeView) tree.getRoot();
        Optional<TreeItem<String>> existingProject = root.getChildren()
                .stream()
                .filter(item -> ((ProjectNodeView) item).getProjectModel().getPath().equals(projectPath))
                .findFirst();
        if (existingProject.isPresent()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Opening Project");
            alert.setHeaderText("Unable to open this project.");
            alert.setContentText("A project with the same name is already open.");
            alert.showAndWait();
        } else {
            ProjectNodeView projectTree = createFolder(projectPath, true); 
            root.getChildren().add(projectTree);
        }
    }
    
    private ProjectNodeView createFolder(Path folderPath, boolean isProject) {
        ProjectModel folderProjectModel = new ProjectModelImpl(folderPath);
        ProjectController folderProjectController = isProject  
                ? new ProjectProjectController(folderProjectModel)
                : new FolderProjectController(folderProjectModel);
        ProjectNodeView folderNode = folderProjectController.getProjectNodeView();
        
        folderNode.setGraphic(Resources.getIcon("close_folder.png")); 
        folderNode.graphicProperty().bind(
                Bindings.when(folderNode.expandedProperty())
                        .then(Resources.getIcon("open_folder.png"))
                        .otherwise(Resources.getIcon("close_folder.png")));
        
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    ProjectNodeView folder = createFolder(path);
                    folderNode.getChildren().add(folder);
                } else {
                    ProjectModel fileProjectModel = new ProjectModelImpl(path);
                    ProjectController fileProjectController = new FileProjectController(fileProjectModel,  
                            editorSetView.getEditorSetController());
                    ProjectNodeView fileNode = fileProjectController.getProjectNodeView();
                    fileNode.setGraphic(Resources.getIcon("file.png"));
                    folderNode.getChildren().add(fileNode);
                }
            }
        } catch (IOException ex) {
            Errors.exceptionDialog("Add Folder Exception", "Unable to add folder: " + folderPath, ex.getMessage(), ex);
        }
        return folderNode;
    }
    
    private ProjectNodeView createFolder(Path folderPath) {
        return createFolder(folderPath, false);
    }
    
    public void removeProject(String name) {
        
    }

    public TreeView<String> getTree() {
        return tree;
    }
}
