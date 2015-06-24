package winter.views.projects;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import winter.Globals;
import winter.views.Errors;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ProjectsPane extends TitledPane {
    private TreeView<Label> tree = new TreeView<>(new TreeItem<>(new Label("Projects")));
    
    public ProjectsPane() {
        setText("Projects");
        setContent(tree);
        prefHeightProperty().bind(Globals.topPane.heightProperty());
        setCollapsible(false);
        
        tree.setShowRoot(false);
    }
    
    public void displayProject(Path projectPath) {
        TreeItem<Label> root = createFolder(projectPath);
        tree.getRoot().getChildren().add(root);
    }
    
    private TreeItem<Label> createFolder(Path folderPath) {
        TreeItem<Label> folderNode = new TreeItem<>(new ProjectNodeValue(folderPath));
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    TreeItem<Label> folder = createFolder(path);
                    folderNode.getChildren().add(folder);
                } else {
                    TreeItem<Label> file = new TreeItem<>(new ProjectNodeValue(path));
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
