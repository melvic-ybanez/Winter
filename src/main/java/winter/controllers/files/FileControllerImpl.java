package winter.controllers.files;

import javafx.stage.FileChooser;
import winter.controllers.projects.ProjectSetController;
import winter.controllers.editors.EditorSetController;
import winter.models.editors.EditorModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.FileUtils;
import winter.views.editor.EditorSetView;
import winter.views.menus.FileMenu;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 7/19/2015.
 */
public class FileControllerImpl implements FileController {
    FileMenu fileMenu;
    
    public FileControllerImpl(EditorSetController editorSetController, ProjectSetController projectSetController) {
        setFileMenu(new FileMenu(this, editorSetController, projectSetController));
    }
    
    @Override
    public void openFile() {
        FileChooser openFileChooser = fileMenu.getOpenFileChooser();
        fileMenu.showOpenDialog().ifPresent(file -> {
            Path path = file.toPath();
            Either<IOException, String> result = FileUtils.openFile(path);
            result.getLeft().ifPresent(Errors::openFileException);
            result.getRight().ifPresent(contents -> {
                EditorSetView editorSetView = fileMenu.getEditorSetController().getEditorSetView();
                editorSetView.newEditorAreaTab(path, contents);
            });
            openFileChooser.setInitialDirectory(file.getParentFile());
        });
    }

    @Override
    public void saveFile() {
        EditorModel editorModel = getEditorSetController().getActiveEditorController().getEditorModel();
        Either<IOException, Boolean> result = FileUtils.saveFile(editorModel.getPath(), editorModel.getContents());
        result.ifLeft(Errors::saveFileException);
        result.ifRight(saved -> {
            if (saved) {
                editorModel.save();
            } else {
                saveAsFile();
            }
        });
    }

    @Override
    public void saveAsFile() {
        FileChooser saveFileChooser = fileMenu.getSaveFileChooser();
        fileMenu.showSaveDialog().ifPresent(file -> {
            Path path = file.toPath();
            EditorModel editorModel = getEditorSetController().getActiveEditorController().getEditorModel();
            Optional<IOException> errorOpt = FileUtils.saveAsFile(path, editorModel.getContents());

            errorOpt.ifPresent(Errors::saveFileException);
            if (!errorOpt.isPresent()) {
                saveFileChooser.setInitialDirectory(file.getParentFile());
                editorModel.setPath(path);
                editorModel.save();
            }
        });
    }

    public FileMenu getFileMenu() {
        return fileMenu;
    }

    public void setFileMenu(FileMenu fileMenu) {
        this.fileMenu = fileMenu;
    }
    
    public EditorSetController getEditorSetController() {
        return fileMenu.getEditorSetController();
    }
}
