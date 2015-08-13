package winter.factories;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import winter.controllers.editors.EditorSetController;
import winter.controllers.projects.ProjectController;
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
    
    private static Consumer<Supplier<Optional<IOException>>> delete(ProjectNodeView projectNodeView, Path path) {
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
                };
            }); 
        };
    }
    
    public static Consumer<ProjectNodeView> deleteFile(Path path) {
        return projectNodeView -> delete(projectNodeView, path).accept(() -> FileUtils.deleteFile(path));
    }
    
    public static Consumer<ProjectNodeView> deleteDirectory(Path path) {
        return projectNodeView -> delete(projectNodeView, path).accept(() -> FileUtils.deleteDirectory(path));
    }
    
    public static Consumer<ProjectNodeView> newFile(Path path) {
        return projectNodeView -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New File");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter filename:");
            Optional<String> answer = dialog.showAndWait();
            answer.ifPresent(filename -> {
                Either<IOException, Path> result = FileUtils.createFile(path.resolve(filename));
                result.ifLeft(Errors::addFileExceptionDialog);
            });
        };
    };

    public static Consumer<ProjectNodeView> newDirectory(Path path) {
        return projectNodeView -> {
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

    public static Consumer<ProjectNodeView> removeProject() {
        return projectNodeView -> {
            projectNodeView.getParent().getChildren().remove(projectNodeView);
        };
    }

    public static Runnable refreshDirectory(ProjectController projectController) {
        return () -> {
            ProjectNodeView projectNodeView = projectController.getProjectNodeView();
            Path path = projectController.getProjectModel().getPath();
            ProjectNodeView parent = (ProjectNodeView) projectNodeView.getParent() ;

            Optional<TreeItem<String>> existingProject = parent.getChildren()
                    .stream()
                    .filter(item -> ((ProjectNodeView) item).getProjectModel().getPath().equals(path))
                    .findFirst();
            if (existingProject.isPresent()) {
                Errors.headerLessDialog("Refreshing Directory", "A directory with the same name already exists");
            } else {

            }
        };
    }
    
    public static Runnable doNothing() {
        return () -> {};
    }
    
    public static <T> Consumer<T> acceptNothing() {
        return whatever -> {};
    }
}
