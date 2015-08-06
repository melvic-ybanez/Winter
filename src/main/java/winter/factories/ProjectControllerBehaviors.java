package winter.factories;

import winter.controllers.editors.EditorSetController;
import winter.models.projects.ProjectModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.FileUtils;
import winter.views.editor.EditorSetView;

import java.io.IOException;

/**
 * Created by ybamelcash on 8/7/2015.
 */
public class ProjectControllerBehaviors {
    public static Runnable openTextFile(ProjectModel projectModel, EditorSetController editorSetController) {
        return () -> {
            Either<IOException, String> result = FileUtils.openFile(projectModel.getPath());
            result.getLeft().ifPresent(Errors::openFileException);
            result.getRight().ifPresent(contents -> {
                EditorSetView editorSetView = editorSetController.getEditorSetView();
                editorSetView.newEditorAreaTab(projectModel.getPath(), contents);
            });    
        };
    }
    
    public static Runnable newFileIn() {
        return () -> {
            
        };
    };
    
    public static Runnable doNothing() {
        return () -> {};
    }
}
