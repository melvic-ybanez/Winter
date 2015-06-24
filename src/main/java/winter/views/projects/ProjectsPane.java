package winter.views.projects;

import javafx.scene.control.*;
import winter.Globals;
import winter.controllers.FileController;
import winter.utils.Either;
import winter.utils.Errors;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class ProjectsPane extends TitledPane {
    private TreeView<ProjectNodeValue> tree = new TreeView<>(new TreeItem<>(new ProjectNodeValue()));
    
    public ProjectsPane() {
        setText("Projects");
        setContent(tree);
        prefHeightProperty().bind(Globals.topPane.heightProperty()); 
        setCollapsible(false);
        
        tree.setShowRoot(false);
        tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<ProjectNodeValue> item = (TreeItem<ProjectNodeValue>) newValue;
            item.getValue().getPath().ifPresent(path -> {
                if (!Files.isDirectory(path)) {
                    Either<IOException, String> result = FileController.openFile(path);
                    result.getLeft().ifPresent(ex -> Errors.openFileException(ex));
                    result.getRight().ifPresent(contents -> {
                        String filename = path.getFileName().toString();
                        TabPane tabPane = Globals.editorPane.getTabPane();
                        Optional<Tab> existingTab = tabPane.getTabs()
                                .stream()
                                .filter(tab -> tab.getText().equals(filename))
                                .findFirst();
                        existingTab.ifPresent(tab -> tabPane.getSelectionModel().select(tab));
                        if (!existingTab.isPresent()) {
                            Globals.editorPane.newEditorAreaTab(filename, contents);
                        }
                    });
                }
            });
        });
    }
    
    public void displayProject(Path projectPath) {
        TreeItem<ProjectNodeValue> root = createFolder(projectPath);
        tree.getRoot().getChildren().add(root);
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
