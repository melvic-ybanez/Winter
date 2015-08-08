package winter.factories;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import winter.controllers.editors.EditorSetController;
import winter.models.projects.ProjectModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.FileUtils;
import winter.views.editor.EditorSetView;
import winter.views.project.ProjectNodeView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
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
    
    private static Function<Supplier<Optional<IOException>>, Runnable> delete(ProjectNodeView projectNodeView, Path path) {
        return (deleteFunction) -> () -> {
            Alert warningDialog = new Alert(Alert.AlertType.CONFIRMATION);
            warningDialog.setTitle("Delete Warning");
            warningDialog.setHeaderText(null);
            warningDialog.setContentText("Are you sure you want to delete " + path);
            Optional<ButtonType> answer = warningDialog.showAndWait();
            answer.ifPresent(button -> {
                if (button == ButtonType.OK) {
                    Optional<IOException> errorOpt = deleteFunction.get();
                    errorOpt.ifPresent(Errors::deleteFileExceptionDialog);
                    if (!errorOpt.isPresent()) {
                        projectNodeView.getParent().getChildren().remove(projectNodeView);
                    }
                };
            }); 
        };
    }
    
    public static Runnable deleteFile(ProjectNodeView projectNodeView, Path path) {
        return delete(projectNodeView, path).apply(() -> FileUtils.deleteFile(path));
    }
    
    public static Runnable deleteDirectory(ProjectNodeView projectNodeView, Path path) {
        return delete(projectNodeView, path).apply(() -> FileUtils.deleteDirectory(path));
    }
    
    public static Runnable newFileIn() {
        return () -> {
            
        };
    };
    
    public static Runnable doNothing() {
        return () -> {};
    }
}
