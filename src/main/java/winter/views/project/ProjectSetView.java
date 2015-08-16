package winter.views.project;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import winter.controllers.projects.ProjectSetController;
import winter.factories.Icons;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.FileUtils;
import winter.views.editor.EditorSetView;

import java.io.IOException;
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
    private ProjectSetController projectSetController;
    
    public ProjectSetView(ProjectSetController projectSetController,
                          EditorSetView editorSetView, ReadOnlyDoubleProperty heightProperty) {
        this.projectSetController = projectSetController;
        this.editorSetView = editorSetView;
        setText("Projects");
        setGraphic(Icons.createProjectsIcon());
        setContent(tree);
        prefHeightProperty().bind(heightProperty); 
        setCollapsible(false);
        
        tree.setRoot(ProjectNodeView.createDummy(projectSetController, editorSetView.getEditorSetController()));
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
    
    public void openProject(Path projectPath, int index) {
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
            root.addDirectory(projectPath, true, index);
        }
    }
    
    public void removeProject(String name) {
        
    }

    public TreeView<String> getTree() {
        return tree;
    }

    public ProjectSetController getProjectSetController() {
        return projectSetController;
    }
}
