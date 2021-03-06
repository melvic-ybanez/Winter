package winter.controllers.editors;

import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import winter.controllers.files.FileController;
import winter.controllers.preferences.GeneralPrefController;
import winter.models.editors.EditorModel;
import winter.utils.Observable;
import winter.utils.SimpleObservable;
import winter.utils.StreamUtils;
import winter.views.editor.EditorSetView;
import winter.views.editor.EditorView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class EditorSetControllerImpl implements EditorSetController {
    private EditorSetView editorSetView;
    private FileController fileController;
    private Observable observable;
    private EditorController previousEditorController;
    private GeneralPrefController generalPrefController;
    
    public EditorSetControllerImpl(GeneralPrefController generalPrefController) {
        setEditorSetView(new EditorSetView(this));
        this.observable = new SimpleObservable();
        this.generalPrefController = generalPrefController;
        editorSetView.getTabPane()
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    int selectedIndex = (Integer) newSelection;
                    int prevSelectedIndex = (Integer) oldSelection;
                    if (selectedIndex != -1) {
                        if (prevSelectedIndex == -1) {
                            previousEditorController = editorSetView.getEditorControllers().get(selectedIndex); 
                        } else {
                            previousEditorController = editorSetView.getEditorControllers().get(prevSelectedIndex);
                        }
                        observable.notifyObservers();
                    }
                });
    }
    
    public EditorController getActiveEditorController() {
        int activeIndex = editorSetView.getTabPane().getSelectionModel().getSelectedIndex();
        return editorSetView.getEditorControllers().get(activeIndex);
    }
    
    public EditorView getActiveEditorView() {
        return getActiveEditorController().getEditorView();
    }
    
    public EditorModel getActiveEditorModel() {
        return getActiveEditorController().getEditorModel();
    }
    
    public boolean closeTab(Tab tab) {
        int index = editorSetView.getTabPane().getTabs().indexOf(tab);
        EditorController editorController = editorSetView.getEditorControllers().get(index);
        
        boolean toClose = editorController.saveAndThen(editorController.getEditorModel(), () -> {
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
        List<EditorController> editorControllers = editorSetView.getEditorControllers();

        // Try to save all the unsaved documents.
        for (EditorController editorController : editorControllers) {
            EditorModel editorModel = editorController.getEditorModel();

            // Save the contents. Don't close the tab. Keep track of the model if the saving fails.
            if (!editorController.saveAndThen(editorModel, () -> {})) {
                unclosedEditors.add(editorModel);
            }
        }

        // If some editors have been closed...
        if (unclosedEditors.size() != editorControllers.size()) {
            editorSetView.getTabPane().getTabs().clear();
            editorControllers.clear();
            unclosedEditors.forEach(editorSetView::newEditorAreaTab);
        }

        return unclosedEditors.isEmpty();
    }

    @Override
    public void goToEditor() {
        EditorModel selectedModel = getActiveEditorModel();
        String defaultName = selectedModel.getTitle();
        Dialog<String> dialog = new Dialog<>();
        dialog.showAndWait();
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

    @Override
    public Observable getObservable() {
        return observable;
    }

    @Override
    public void selectTab(EditorModel editorModel) {
        List<EditorController> editorControllers = editorSetView.getEditorControllers();
        StreamUtils.find(IntStream.range(0, editorControllers.size()).boxed(), i -> {
            EditorModel editorModel1 = editorControllers.get(i).getEditorModel();
            return editorModel == editorModel1;
        }).ifPresent(i -> {
            editorSetView.getTabPane().getSelectionModel().select(i);
            getActiveEditorView().requestFocus();
        });
    }

    @Override
    public GeneralPrefController getGeneralPrefController() {
        return generalPrefController;
    }

    @Override
    public EditorController getPreviousEditorController() {
        return previousEditorController;
    }
}
