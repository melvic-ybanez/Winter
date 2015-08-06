package winter.views.project;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import winter.Resources;
import winter.factories.Icons;
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
    private TreeView<ProjectNodeValue> tree = new TreeView<>(new TreeItem<>(new ProjectNodeValue()));
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
        
        createContextMenu();
        
        tree.setShowRoot(false);
        registerEvents();
        
        managedProperty().bind(visibleProperty());
    }
    
    private void registerEvents() {
        tree.setOnMouseClicked(event -> {
            TreeItem<ProjectNodeValue> item = tree.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                item.getValue().getPath().ifPresent(path -> {
                    if (!Files.isDirectory(path)) {
                        Either<IOException, String> result = FileUtils.openFile(path);
                        result.getLeft().ifPresent(Errors::openFileException);
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
                });
            } else if (event.getButton() == MouseButton.SECONDARY) {
                
            }
        });
    }
    
    private void createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem newFileItem = new MenuItem("New File...");
        MenuItem newFolderItem = new MenuItem("New Folder...");
        MenuItem renameItem = new MenuItem("Rename...");
        MenuItem moveItem = new MenuItem("Move...");
        MenuItem deleteItem = new MenuItem("Delete");
        
        contextMenu.getItems().addAll(newFileItem,
                newFolderItem, new SeparatorMenuItem(),
                renameItem, moveItem,
                new SeparatorMenuItem(), deleteItem);
        
        tree.contextMenuProperty().bind(
                Bindings.when(tree.getRoot().leafProperty())
                .then((ContextMenu) null)
                .otherwise(contextMenu)
        );
    }
    
    private ContextMenu createFolderContextMenu() {
        if (folderContextMenu == null) {
            folderContextMenu = new ContextMenu();
            MenuItem newFileItem = new MenuItem("New File...");
            MenuItem newFolderItem = new MenuItem("New Folder...");
            MenuItem renameItem = new MenuItem("Rename...");
            MenuItem moveItem = new MenuItem("Move...");
            MenuItem deleteItem = new MenuItem("Delete");

            folderContextMenu.getItems().addAll(newFileItem,
                    newFolderItem, new SeparatorMenuItem(),
                    renameItem, moveItem,
                    new SeparatorMenuItem(), deleteItem);
        }
        return folderContextMenu;
    }
    
    public void displayProject(Path projectPath) {
        TreeItem<ProjectNodeValue> root = tree.getRoot();
        Optional<TreeItem<ProjectNodeValue>> existingProject = root.getChildren()
                .stream()
                .filter(item -> item.getValue().getPath().equals(Optional.of(projectPath)))
                .findFirst();
        if (existingProject.isPresent()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Opening Project");
            alert.setHeaderText("Unable to open this project.");
            alert.setContentText("A project with the same name is already open.");
            alert.showAndWait();
        } else {
            TreeItem<ProjectNodeValue> projectTree = createFolder(projectPath);
            root.getChildren().add(projectTree);
        }
    }
    
    private TreeItem<ProjectNodeValue> createFolder(Path folderPath) {
        TreeItem<ProjectNodeValue> folderNode = new TreeItem<>(new ProjectNodeValue(folderPath));
        
        folderNode.setGraphic(Resources.getIcon("close_folder.png")); 
        folderNode.graphicProperty().bind(
                Bindings.when(folderNode.expandedProperty())
                        .then(Resources.getIcon("open_folder.png"))
                        .otherwise(Resources.getIcon("close_folder.png")));
        
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    TreeItem<ProjectNodeValue> folder = createFolder(path);
                    folderNode.getChildren().add(folder);
                } else {
                    TreeItem<ProjectNodeValue> file = new TreeItem<>(new ProjectNodeValue(path));
                    file.setGraphic(Resources.getIcon("file.png"));
                    folderNode.getChildren().add(file);
                }
            }
        } catch (IOException ex) {
            Errors.exceptionDialog("Add Folder Exception", "Unable to add folder: " + folderPath, ex.getMessage(), ex);
        }
        return folderNode;
    }
    
    public void removeProject(String name) {
        
    }
}
