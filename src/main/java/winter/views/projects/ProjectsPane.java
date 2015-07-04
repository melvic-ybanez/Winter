package winter.views.projects;

import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import winter.Globals;
import winter.controllers.FileController;
import winter.utils.Either;
import winter.utils.Errors;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ProjectsPane extends TitledPane {
    private TreeView<ProjectNodeValue> tree = new TreeView<>(new TreeItem<>(new ProjectNodeValue()));
    
    public ProjectsPane() {
        setText("Projects");
        setContent(tree);
        prefHeightProperty().bind(Globals.topSplitPane.heightProperty()); 
        setCollapsible(false);
        
        createContextMenu();
        
        tree.setShowRoot(false);
        tree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                TreeItem<ProjectNodeValue> item = tree.getSelectionModel().getSelectedItem();
                item.getValue().getPath().ifPresent(path -> {
                    if (!Files.isDirectory(path)) {
                        Either<IOException, String> result = FileController.openFile(path);
                        result.getLeft().ifPresent(Errors::openFileException);
                        result.getRight().ifPresent(contents -> {
                            String filename = path.getFileName().toString();
                            TabPane tabPane = Globals.editorPane.getTabPane();
                            Optional<Tab> existingTab = tabPane.getTabs()
                                    .stream()
                                    .filter(tab -> tab.getText().equals(filename))
                                    .findFirst();
                            existingTab.ifPresent(tab -> tabPane.getSelectionModel().select(tab));
                            if (!existingTab.isPresent()) {
                                Globals.editorPane.newEditorAreaTab(path, contents);
                            }
                        });
                    }
                });
            }
        });
        setOpacity(0.78);
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
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    TreeItem<ProjectNodeValue> folder = createFolder(path);
                    folderNode.getChildren().add(folder);
                } else {
                    TreeItem<ProjectNodeValue> file = new TreeItem<>(new ProjectNodeValue(path));
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
