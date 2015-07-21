package winter.views.editor;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import winter.controllers.edit.FindController;
import winter.controllers.edit.FindControllerImpl;
import winter.controllers.editor.EditorController;
import winter.controllers.editor.EditorControllerImpl;
import winter.controllers.editor.EditorSetController;
import winter.models.EditorModel;
import winter.models.FindModelImpl;
import winter.models.MeruemEditorModel;
import winter.utils.Either;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class EditorSetView extends BorderPane {
    private EditorSetController editorSetController;
    private TabPane tabPane = new TabPane();
    private List<EditorController> editorControllers = new ArrayList<>();
    private int untitledCount = 0;
    
    public EditorSetView(EditorSetController editorSetController) {
        setEditorSetController(editorSetController);
        setCenter(tabPane);
        
        createContextMenu();
        tabPane.getStyleClass().add("meruem-tabpane");
    }
    
    private void createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem closeOtherItem = new MenuItem("Close Others");
        MenuItem closeAllItem = new MenuItem("Close All");
        
        renameItem.setOnAction(e -> editorSetController.getActiveEditorController().rename());
        closeItem.setOnAction(e -> closeCurrentTab());
        closeOtherItem.setOnAction(e -> editorSetController.closeOtherTabs());
        closeAllItem.setOnAction(e -> closeAllTabs());
        
        closeItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        
        contextMenu.getItems().addAll(renameItem, closeItem, closeOtherItem, closeAllItem);
        tabPane.setContextMenu(contextMenu);
    }
    
    public void newEditorAreaTab(Either<Integer, Path> pathEither, String contents) {
        EditorModel editorModel = new MeruemEditorModel(pathEither);
        EditorController editorController = new EditorControllerImpl(editorModel, editorSetController);
        editorController.setFileController(editorSetController.getFileController());
        Optional<Path> pathOpt = editorModel.getPath();
        String title = editorModel.titleProperty().getValue();
        
        if (pathOpt.isPresent() && editorSetController.exists(pathOpt.get())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("File already exists.");
            alert.setHeaderText("The file " + title + " already exists.");
            alert.setContentText("Do you want to replace it with the new one?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                tabPane.getTabs().removeIf(tab -> tab.getText().equals(title));
                editorControllers = editorSetController.remove(pathOpt.get());
                newEditorAreaTab(pathEither, contents);
            } 
        } else {
            Tab tab = new Tab(title);
            editorControllers.add(editorController);
            CodeArea codeArea = editorController.getEditorView();
            codeArea.replaceText(0, 0, contents);
              
            tab.textProperty().bind(editorModel.titleProperty());
            tab.setGraphic(new Label()); 
            tab.graphicProperty().bindBidirectional(editorController.getEditorView().graphicProperty());
            
            editorModel.contentsProperty().bind(codeArea.textProperty());
            editorModel.caretPositionProperty().bind(codeArea.caretPositionProperty());
            editorModel.setOrigContents(contents);
            
            tabPane.getTabs().add(tab); 
            tabPane.getSelectionModel().select(tab); 
            
            FindController findController = new FindControllerImpl(
                    new FindModelImpl(codeArea.getCaretPosition()), editorController);
            editorController.getEditorView().setFindView(findController.getFindView());
            
            BorderPane tabContentPane = new BorderPane();
            tabContentPane.setCenter(codeArea);
            tabContentPane.setBottom(findController.getFindView());
            tab.setContent(tabContentPane);
            
            tab.textProperty().bind(editorModel.titleProperty());
            tab.setOnCloseRequest(event -> {
                if (!editorSetController.closeTab(tab)) event.consume();
            });
        }
    }
    
    public void newEditorAreaTab(EditorModel model) {
        newEditorAreaTab(model.getPathEither(), model.getOrigContents());
        editorSetController.getActiveEditorView().replaceText(model.getContents());
    }
    
    public void newEditorAreaTab(Path path, String contents) {
        newEditorAreaTab(Either.right(path), contents);
    }

    public void newUntitledTab(int untitledCount) {
        newEditorAreaTab(Either.left(untitledCount), "");
        this.untitledCount = ++untitledCount;
    }
    
    public void newUntitledTab() {
        newUntitledTab(this.untitledCount);
    }
    
    private boolean closeCurrentTab() {
        Tab selectedTab = getTabPane().getSelectionModel().getSelectedItem();
        return editorSetController.closeTab(selectedTab);
    }
    
    private void closeAllTabs() {
        editorSetController.closeAllTabs();
        if (editorControllers.isEmpty()) {
            newUntitledTab();
        }
    }
    
    public TabPane getTabPane() {
        return tabPane;
    }
    
    public List<EditorController> getEditorControllers() {
        return editorControllers;
    }
    
    public void setEditorControllers(List<EditorController> editorControllers) {
        this.editorControllers = editorControllers;
    }

    public void setEditorSetController(EditorSetController editorSetController) {
        this.editorSetController = editorSetController;
    }
}
