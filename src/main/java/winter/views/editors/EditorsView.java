package winter.views.editors;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import winter.controllers.*;
import winter.models.EditorModel;
import winter.models.MeruemEditorModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.views.edit.FindPane;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class EditorsView extends BorderPane {
    private EditorsController editorsController;
    private TabPane tabPane = new TabPane();
    private List<EditorController> editorControllers = new ArrayList<>();
    private int untitledCount = 0;
    
    public EditorsView(EditorsController editorsController) {
        setEditorsController(editorsController);
        setCenter(tabPane);
        setBottom(new FindPane());
        createContextMenu();
        newUntitledTab();
        tabPane.getStyleClass().add("meruem-tabpane");
    }
    
    private void createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem closeOtherItem = new MenuItem("Close Others");
        MenuItem closeAllItem = new MenuItem("Close All");
        
        renameItem.setOnAction(e -> renameTab());
        closeItem.setOnAction(e -> closeCurrentTab());
        closeOtherItem.setOnAction(e -> EditorsControllerImpl.closeOtherTabs());
        closeAllItem.setOnAction(e -> closeAllTabs());
        
        closeItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        
        contextMenu.getItems().addAll(renameItem, closeItem, closeOtherItem, closeAllItem);
        tabPane.setContextMenu(contextMenu);
    }
    
    public void newEditorAreaTab(Either<Integer, Path> pathEither, String contents) {
        EditorModel editorModel = new MeruemEditorModel(pathEither);
        EditorController editorController = new EditorControllerImpl(editorModel);
        Optional<Path> pathOpt = editorModel.getPath();
        String title = editorModel.titleProperty().getValue();
        
        if (pathOpt.isPresent() && editorsController.exists(pathOpt.get())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("File already exists.");
            alert.setHeaderText("The file " + title + " already exists.");
            alert.setContentText("Do you want to replace it with the new one?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                tabPane.getTabs().removeIf(tab -> tab.getText().equals(title));
                editorControllers = editorsController.remove(pathOpt.get());
                newEditorAreaTab(pathEither, contents);
            } 
        } else {
            Tab tab = new Tab(title);
            editorControllers.add(editorModel);
            CodeArea codeArea = editorController.getEditorView();
            codeArea.replaceText(0, 0, contents); 
            
            tab.setContent(codeArea); 
            tab.textProperty().bind(editorModel.titleProperty());
            
            tab.setGraphic(new Label()); 
            tab.graphicProperty().bindBidirectional(editorController.getEditorView().graphicProperty());
            
            editorModel.contentsProperty().bind(codeArea.textProperty());
            editorModel.caretPositionProperty().bind(codeArea.caretPositionProperty());
            editorModel.setOrigContents(contents);
            
            tabPane.getTabs().add(tab); 
            tabPane.getSelectionModel().select(tab);
            
            tab.textProperty().bind(editorModel.titleProperty());
            tab.setOnCloseRequest(event -> {
                if (!EditorsControllerImpl.closeTab(tab)) event.consume();
            });
        }
    }
    
    public void newEditorAreaTab(EditorModel model) {
        newEditorAreaTab(model.getPathEither(), model.getOrigContents());
        EditorsControllerImpl.getActiveCodeArea().replaceText(model.getContents());
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
        return EditorsControllerImpl.closeTab(selectedTab);
    }
    
    private void closeAllTabs() {
        EditorsControllerImpl.closeAllTabs();
        if (editorControllers.isEmpty()) {
            newUntitledTab();
        }
    }
    
    private void renameTab() {
        MeruemEditorModel activeEditor = EditorsControllerImpl.getActiveEditor();
        activeEditor.ifUntitled(count -> {
            Errors.headerLessDialog(Errors.titles.RENAME, "Can not rename a non-existing file");
        });
        activeEditor.getPath().ifPresent(path -> {
            TextInputDialog renameDialog = new TextInputDialog(activeEditor.getTitle());
            renameDialog.setTitle("Rename File");
            renameDialog.setHeaderText(null);
            renameDialog.setContentText("Enter the new filename");

            Optional<String> newFilenameOpt = renameDialog.showAndWait();
            newFilenameOpt.ifPresent(newFilename -> {
                Either<IOException, Either<String, Path>> result = FileController.renameFile(path, newFilename);
                result.ifLeft(ex -> Errors.exceptionDialog(Errors.titles.RENAME, null, ex.getMessage(), ex));
                result.ifRight(right -> {
                    right.ifLeft(error -> Errors.headerLessDialog(Errors.titles.RENAME, error));
                    right.ifRight(activeEditor::setPath);
                });
            });    
        });
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

    public void setEditorsController(EditorsController editorsController) {
        this.editorsController = editorsController;
    }
}
