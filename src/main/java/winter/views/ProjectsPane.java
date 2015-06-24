package winter.views;

import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import winter.Globals;
import winter.controllers.ProjectController;
import winter.models.ProjectModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ProjectsPane extends TitledPane {
    private TreeView<String> tree = new TreeView<>(new TreeItem<>("Projects"));
    
    public ProjectsPane() {
        setText("Projects");
        setContent(tree);
        prefHeightProperty().bind(Globals.topPane.heightProperty());
        setCollapsible(false);
        
        tree.setShowRoot(false);
    }
    
    public void displayProject(Path projectPath) {
        TreeItem<String> root = createFolder(projectPath);
        tree.getRoot().getChildren().add(root);
    }
    
    private TreeItem<String> createFolder(Path folderPath) {
        TreeItem<String> root = new TreeItem<>(folderPath.toString());
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    TreeItem<String> folder = createFolder(path);
                    root.getChildren().add(folder);
                } else {
                    TreeItem<String> file = new TreeItem<>(path.toString());
                    root.getChildren().add(file);
                }
            }
        } catch (IOException ex) {
            Errors.exceptionDialog("Add Folder Exception", "Unable to add folder: " + folderPath, ex.getMessage(), ex);
        }
        return root;
    }
    
    public void removeProject(String name) {
        
    }
}
