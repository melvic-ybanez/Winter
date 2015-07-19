package winter.controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;
import winter.models.EditorModel;
import winter.utils.StreamUtils;
import winter.views.editors.EditorSetView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class EditorSetControllerImpl implements EditorSetController {
    private EditorSetView editorSetView;
    private FileController fileController;
    
    public EditorSetControllerImpl() {
        setEditorSetView(new EditorSetView(this));
    }
    
    public EditorController getActiveEditorController() {
        int activeIndex = editorSetView.getTabPane().getSelectionModel().getSelectedIndex();
        return editorSetView.getEditorControllers().get(activeIndex);
    }
    
    public CodeArea getActiveCodeArea() {
        return (CodeArea) editorSetView.getTabPane().getSelectionModel().getSelectedItem().getContent();
    }
    
    public boolean closeTab(Tab tab) {
        int index = editorSetView.getTabPane().getTabs().indexOf(tab);
        EditorController editorController = editorSetView.getEditorControllers().get(index);
        
        boolean toClose = editorController.whenHasChanges(() -> {
            editorSetView.getTabPane().getTabs().remove(tab);
            final List<EditorController> editors = editorSetView.getEditorControllers();
            editorController.getEditorModel().getPath().ifPresent(path -> {
                editorSetView.setEditorControllers(remove(path));
            });
            if (!editorController.getEditorModel().getPath().isPresent())
                editorSetView.setEditorControllers(editors.stream().filter(controller -> 
                        !controller.getEditorModel().getTitle().equals(editorController.getEditorModel().getTitle()))
                        .collect(Collectors.toList()));
        });

        if (editorSetView.getEditorControllers().isEmpty()) {
            editorSetView.newUntitledTab();
        }
        
        return toClose;
    }
    
    public void closeOtherTabs() {
        EditorController editorController = getActiveEditorController();
        
        int selectedIndex = editorSetView.getTabPane().getSelectionModel().getSelectedIndex();
        editorSetView.getTabPane().getTabs().remove(selectedIndex);
        editorSetView.getEditorControllers().remove(selectedIndex);
        
        closeAllTabs();
        editorSetView.newEditorAreaTab(editorController.getEditorModel());
    }
    
    public boolean closeAllTabs() {
        List<EditorModel> unclosedEditors = new ArrayList<>();
        ObservableList<Tab> tabs = editorSetView.getTabPane().getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            EditorController editorController = editorSetView.getEditorControllers().get(i);
            if (!editorController.whenHasChanges(() -> {})) {
                unclosedEditors.add(editorController.getEditorModel());
            }
        }
        if (unclosedEditors.size() != tabs.size()) {
            editorSetView.getTabPane().getTabs().clear();
            editorSetView.getEditorControllers().clear();
            unclosedEditors.forEach(editorSetView::newEditorAreaTab);
        }
        return unclosedEditors.isEmpty();
    }
    
    public Optional<EditorController> find(Path path) {
        return StreamUtils.find(editorSetView.getEditorControllers().stream(), 
                controller -> controller.getEditorModel().equalsPath(path));
    }
    
    public boolean exists(Path path) {
        return StreamUtils.exists(editorSetView.getEditorControllers().stream(),
                controller -> controller.getEditorModel().equalsPath(path));
    }
    
    public List<EditorController> remove(Path path) {
        return StreamUtils.remove(editorSetView.getEditorControllers().stream(), 
                controller -> controller.getEditorModel().equalsPath(path)).collect(Collectors.toList());
    }

    public EditorSetView getEditorSetView() {
        return editorSetView;
    }

    public void setEditorSetView(EditorSetView editorSetView) {
        this.editorSetView = editorSetView;
    }
    
    public void setFileController(FileController fileController) {
        this.fileController = fileController;
        editorSetView.getEditorControllers().forEach(editorController ->
                editorController.setFileController(fileController));
    }

    public FileController getFileController() {
        return fileController;
    }
}
