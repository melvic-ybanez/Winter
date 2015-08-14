package winter.factories;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import winter.controllers.editors.EditorSetController;
import winter.controllers.projects.ProjectController;
import winter.controllers.projects.ProjectSetController;
import winter.models.projects.ProjectModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.FileUtils;
import winter.views.editor.EditorSetView;
import winter.views.project.ProjectNodeView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by ybamelcash on 8/7/2015.
 */
public class ProjectControllerBehaviors {
    public static Runnable openTextFile(ProjectModel projectModel, EditorSetController editorSetController) {
        return () -> {
            Either<IOException, String> result = FileUtils.openFile(projectModel.getPath());
            result.getLeft().ifPresent(Errors::openFileExceptionDialog);
            result.getRight().ifPresent(contents -> {
                EditorSetView editorSetView = editorSetController.getEditorSetView();
                editorSetView.newEditorAreaTab(projectModel.getPath(), contents);
            });    
        };
    }
    
    private static Consumer<Supplier<Optional<IOException>>> delete(Path path) {
        return (deleteFunction) -> {
            Alert warningDialog = new Alert(Alert.AlertType.CONFIRMATION);
            warningDialog.setTitle("Delete Warning");
            warningDialog.setHeaderText(null);
            warningDialog.setContentText("Are you sure you want to delete " + path);
            Optional<ButtonType> answer = warningDialog.showAndWait();
            answer.ifPresent(button -> {
                if (button == ButtonType.OK) {
                    Optional<IOException> errorOpt = deleteFunction.get();
                    errorOpt.ifPresent(Errors::deleteFileExceptionDialog);
                }
                ;
            }); 
        };
    }
    
    public static Runnable deleteFile(Path path) {
        return () -> delete(path).accept(() -> FileUtils.deleteFile(path));
    }
    
    public static Runnable deleteDirectory(Path path) {
        return () -> delete(path).accept(() -> FileUtils.deleteDirectory(path));
    }
    
    public static Runnable newFile(Path path) {
        return () -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New File");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter Filename:");
            Optional<String> answer = dialog.showAndWait();
            answer.ifPresent(filename -> {
                Either<IOException, Path> result = FileUtils.createFile(path.resolve(filename));
                result.ifLeft(Errors::addFileExceptionDialog);
            });
        };
    };

    public static Runnable newDirectory(Path path) {
        return () -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New Directory");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter Directory Name:");
            Optional<String> answer = dialog.showAndWait();
            answer.ifPresent(directoryName -> {
                Either<IOException, Path> result = FileUtils.createDirectory(path.resolve(directoryName));
                result.ifLeft(Errors::addDirectoryExceptionDialog);
            });
        };
    };

    public static Runnable renameFile(Path path) {
        return () -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Rename File");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter new Filename:");
            Optional<String> answer = dialog.showAndWait();
            answer.ifPresent(filename -> {
                Either<IOException, Path> result = FileUtils.renameFile(path, filename);
                result.ifLeft(Errors::addFileExceptionDialog);
            });
        };
    };

    public static Runnable renameDirectory(Path path) {
        return () -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Rename Directory");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter new Directory Name:");
            Optional<String> answer = dialog.showAndWait();
            answer.ifPresent(directoryName -> {
                Either<IOException, Path> result = FileUtils.renameFile(path, directoryName);
                result.ifLeft(Errors::addDirectoryExceptionDialog);
            });
        };
    };

    public static Consumer<ProjectNodeView> removeProject() {
        return projectNodeView -> {
            projectNodeView.getParent().getChildren().remove(projectNodeView);
        };
    }

    public static Runnable removeAllProjects(ProjectSetController projectSetController) {
        return () -> {
            TreeView<String> tree = projectSetController.getProjectSetView().getTree();
            ObservableList<TreeItem<String>> children = tree.getRoot().getChildren();
            children.clear();
        };
    }

    public static Runnable refreshDirectory(ProjectController projectController) {
        return () -> {
            Path path = projectController.getProjectModel().getPath();
            ProjectNodeView projectNodeView = projectController.getProjectNodeView();
            int index = projectNodeView.getParent().getChildren().indexOf(projectNodeView);
            projectController.close();
            projectController.getProjectSetController().openProject(path, index);
        };
    }
    
    public static Runnable doNothing() {
        return () -> {};
    }
    
    public static <T> Consumer<T> acceptNothing() {
        return whatever -> {};
    }
}
