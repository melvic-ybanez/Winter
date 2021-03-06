package winter.views.editor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import winter.controllers.editors.EditorController;
import winter.controllers.editors.EditorControllerImpl;
import winter.controllers.editors.EditorSetController;
import winter.controllers.edits.FindController;
import winter.controllers.edits.FindControllerImpl;
import winter.controllers.edits.ReplaceController;
import winter.controllers.edits.ReplaceControllerImpl;
import winter.models.editors.EditorModel;
import winter.models.editors.MeruemEditorModel;
import winter.models.edits.FindModelImpl;
import winter.models.edits.ReplaceModelImpl;
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
    private ContextMenu tabContextMenu;
    private ContextMenu editorContextMenu = new ContextMenu();
    private StringProperty fontStyleProperty = new SimpleStringProperty();
    
    public EditorSetView(EditorSetController editorSetController) {
        setEditorSetController(editorSetController);
        setCenter(tabPane);
        
        createTabContextMenu();
        tabPane.getStyleClass().add("meruem-tabpane");
    }
    
    private void createTabContextMenu() {
        tabContextMenu = new ContextMenu();
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem closeOtherItem = new MenuItem("Close Others");
        MenuItem closeAllItem = new MenuItem("Close All");
        
        renameItem.setOnAction(e -> editorSetController.getActiveEditorController().rename());
        closeItem.setOnAction(e -> closeCurrentTab());
        closeOtherItem.setOnAction(e -> editorSetController.closeOtherTabs());
        closeAllItem.setOnAction(e -> closeAllTabs());
        
        closeItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        
        tabContextMenu.getItems().addAll(renameItem, closeItem, closeOtherItem, closeAllItem);
    }
    
    public void newEditorAreaTab(Either<Integer, Path> pathEither, String contents) {
        EditorModel editorModel = new MeruemEditorModel(pathEither,
                editorSetController.getGeneralPrefController().getGeneralPrefModel());
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
            editorModel.setOrigContents(contents);
            editorModel.removeExtraSpacesProperty().bind(editorSetController.getGeneralPrefController()
                    .getGeneralPrefModel().removeExtraSpacesProperty());
            editorControllers.add(editorController);
            
            CodeArea codeArea = editorController.getEditorView();
            codeArea.replaceText(0, 0, contents);
            codeArea.setContextMenu(editorContextMenu);

            Tab tab = new Tab(title);
            tab.textProperty().bind(editorModel.titleProperty());
            tab.setGraphic(new Label()); 
            tab.graphicProperty().bindBidirectional(editorController.getEditorView().graphicProperty());
            
            tabPane.getTabs().add(tab); 
            tabPane.getSelectionModel().select(tab); 
            
            FindController findController = new FindControllerImpl(
                    new FindModelImpl(), editorController);
            ReplaceController replaceController = new ReplaceControllerImpl(new ReplaceModelImpl(), 
                    findController.getFindView());
            editorController.getEditorView().setReplaceView(replaceController.getReplaceView());
            
            BorderPane tabContentPane = new BorderPane();
            tabContentPane.setCenter(codeArea);
            tabContentPane.setBottom(replaceController.getReplaceView());
            tab.setContent(tabContentPane);
            
            tab.textProperty().bind(editorModel.titleProperty());
            tab.setOnCloseRequest(event -> {
                if (!editorSetController.closeTab(tab)) event.consume();
            });
            tab.setContextMenu(tabContextMenu);
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

    public EditorSetController getEditorSetController() {
        return editorSetController;
    }

    public ContextMenu getEditorContextMenu() {
        return editorContextMenu;
    }

    public StringProperty fontStyleStringProperty() {
        return fontStyleProperty;
    }

    public void setFontStyleString(String fontStyleString) {
        fontStyleProperty.set(fontStyleString);
    }
}
