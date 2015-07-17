package winter.controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import winter.Application;
import winter.models.EditorModel;
import winter.models.MeruemEditorModel;
import winter.utils.*;
import winter.views.editors.EditorsView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class EditorsControllerImpl implements EditorsController {
    private EditorsView editorsView;
    
    public EditorsControllerImpl() {
        editorsView = new EditorsView(this);
    }
    
    public EditorController getActiveEditorController() {
        int activeIndex = editorsView.getTabPane().getSelectionModel().getSelectedIndex();
        return editorsView.getEditorControllers().get(activeIndex);
    }
    
    public CodeArea getActiveCodeArea() {
        return (CodeArea) editorsView.getTabPane().getSelectionModel().getSelectedItem().getContent();
    }
    
    public static void openFile() {
        FileChooser openFileChooser = Application.menus.fileMenu.getOpenFileChooser();
        Application.menus.fileMenu.showOpenDialog().ifPresent(file -> {
            Path path = file.toPath();
            Either<IOException, String> result = FileController.openFile(path);
            result.getLeft().ifPresent(Errors::openFileException);
            result.getRight().ifPresent(contents -> {
                Application.EDITORS_VIEW.newEditorAreaTab(path, contents);
            });
            openFileChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    public static void saveFile(MeruemEditorModel editorModel) {
        Either<IOException, Boolean> result = FileController.saveFile(editorModel.getPath(), editorModel.getContents());
        result.ifLeft(Errors::saveFileException);
        result.ifRight(saved -> {
            if (saved) {
                editorModel.save();
            } else {
                saveAsFile(editorModel);
            } 
        });
    }
    
    public static void saveAsFile(MeruemEditorModel editorModel) {
        FileChooser saveFileChooser = Application.menus.fileMenu.getSaveFileChooser();
        Application.menus.fileMenu.showSaveDialog().ifPresent(file -> {
            Path path = file.toPath();
            Optional<IOException> errorOpt = FileController.saveAsFile(path, editorModel.getContents());

            errorOpt.ifPresent(Errors::saveFileException);
            if (!errorOpt.isPresent()) {
                saveFileChooser.setInitialDirectory(file.getParentFile());
                EditorsControllerImpl.renameSelectedTab(path);
                editorModel.save();
            }
        });
    }
    
    public boolean closeTab(Tab tab) {
        int index = editorsView.getTabPane().getTabs().indexOf(tab);
        EditorController editorController = editorsView.getEditorControllers().get(index);
        
        boolean toClose = editorController.whenHasChanges(() -> {
            editorsView.getTabPane().getTabs().remove(tab);
            final List<EditorController> editors = editorsView.getEditorControllers();
            editorController.getEditorModel().getPath().ifPresent(path -> {
                editorsView.setEditorControllers(remove(path));
            });
            if (!editorController.getEditorModel().getPath().isPresent())
                editorsView.setEditorControllers(editors.stream().filter(controller -> 
                        !controller.getEditorModel().getTitle().equals(editorController.getEditorModel().getTitle()))
                        .collect(Collectors.toList()));
        });

        if (editorsView.getEditorControllers().isEmpty()) {
            editorsView.newUntitledTab();
        }
        
        return toClose;
    }
    
    public void closeOtherTabs() {
        EditorController editorController = getActiveEditorController();
        
        int selectedIndex = editorsView.getTabPane().getSelectionModel().getSelectedIndex();
        editorsView.getTabPane().getTabs().remove(selectedIndex);
        editorsView.getEditorControllers().remove(selectedIndex);
        
        closeAllTabs();
        editorsView.newEditorAreaTab(editorController.getEditorModel());
    }
    
    public boolean closeAllTabs() {
        List<EditorModel> unclosedEditors = new ArrayList<>();
        ObservableList<Tab> tabs = editorsView.getTabPane().getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            EditorController editorController = editorsView.getEditorControllers().get(i);
            if (!editorController.whenHasChanges(() -> {})) {
                unclosedEditors.add(editorController.getEditorModel());
            }
        }
        if (unclosedEditors.size() != tabs.size()) {
            editorsView.getTabPane().getTabs().clear();
            editorsView.getEditorControllers().clear();
            unclosedEditors.forEach(editorsView::newEditorAreaTab);
        }
        return unclosedEditors.isEmpty();
    }
    
    public Optional<EditorController> find(Path path) {
        return StreamUtils.find(editorsView.getEditorControllers().stream(), 
                controller -> controller.getEditorModel().equalsPath(path));
    }
    
    public boolean exists(Path path) {
        return StreamUtils.exists(editorsView.getEditorControllers().stream(),
                controller -> controller.getEditorModel().equalsPath(path));
    }
    
    public List<EditorController> remove(Path path) {
        return StreamUtils.remove(editorsView.getEditorControllers().stream(), 
                controller -> controller.getEditorModel().equalsPath(path)).collect(Collectors.toList());
    }
}
