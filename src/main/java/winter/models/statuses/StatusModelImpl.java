package winter.models.statuses;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import winter.controllers.editors.EditorSetController;
import winter.models.editors.EditorModel;
import winter.utils.Observable;
import winter.utils.Observer;
import winter.utils.SimpleObservable;

import javax.swing.event.ChangeListener;

/**
 * Created by ybamelcash on 8/2/2015.
 */
public class StatusModelImpl extends SimpleObservable implements StatusModel {
    private IntegerProperty lineNumberProperty = new SimpleIntegerProperty();
    private IntegerProperty columnNumberProperty = new SimpleIntegerProperty();
    
    private Observable lineNumberObservable = new SimpleObservable();
    private Observable textObservable = new SimpleObservable();
    
    private boolean changesSaved;
    
    private EditorSetController editorSetController;

    private Observer activeModelListener = () -> {
        EditorModel activeModel = editorSetController.getActiveEditorModel();
        if (!activeModel.unsaved()) {
            textObservable.notifyObservers();
            changesSaved = true;
        }
    };
    
    public StatusModelImpl(EditorSetController editorSetController) {
        this.editorSetController = editorSetController;
        registerObservers();
    }
    
    private void registerObservers() {
        editorSetController.getObservable().registerObserver(() -> {
            EditorModel activeModel = editorSetController.getActiveEditorModel();
            lineNumberProperty.bind(activeModel.lineNumberProperty());
            columnNumberProperty.bind(activeModel.columnNumberProperty());
            lineNumberObservable.notifyObservers();

            // Listen to the new active editor model
            editorSetController.getPreviousEditorController().getEditorModel().removeObserver(activeModelListener);
            activeModel.registerObserver(activeModelListener);
            changesSaved = !activeModel.unsaved() && !activeModel.isUntitled();    
        });
        
        Runnable notifyLineNumberObservers = () -> {
            changesSaved = !editorSetController.getActiveEditorModel().unsaved();
            lineNumberObservable.notifyObservers();
        };

        lineNumberProperty.addListener((observable, oldLineNumber, newLineNumber) ->
                notifyLineNumberObservers.run());
        columnNumberProperty.addListener((observable, oldColumnNumber, newColumnNumber) ->
                notifyLineNumberObservers.run());
    }

    @Override
    public IntegerProperty lineNumberProperty() {
        return lineNumberProperty;
    }

    @Override
    public IntegerProperty columnNumberProperty() {
        return columnNumberProperty;
    }

    @Override
    public int getLineNumber() {
        return lineNumberProperty.get();
    }

    @Override
    public int getColumnNumber() {
        return columnNumberProperty.get();
    }

    @Override
    public EditorSetController getEditorSetController() {
        return editorSetController;
    }

    @Override
    public boolean areChangesSaved() {
        return changesSaved;
    }

    @Override
    public void setEditorSetController(EditorSetController editorSetController) {
        this.editorSetController = editorSetController;
    }

    @Override
    public Observable getLineNumberObservable() {
        return lineNumberObservable;
    }

    @Override
    public Observable getTextObservable() {
        return textObservable;
    }
}
