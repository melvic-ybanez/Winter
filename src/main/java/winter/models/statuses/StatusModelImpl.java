package winter.models.statuses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import winter.controllers.editors.EditorSetController;
import winter.models.editors.EditorModel;
import winter.utils.Observer;
import winter.utils.SimpleObservable;

import javax.swing.event.ChangeListener;

/**
 * Created by ybamelcash on 8/2/2015.
 */
public class StatusModelImpl extends SimpleObservable implements StatusModel, Observer {
    private IntegerProperty lineNumberProperty = new SimpleIntegerProperty();
    private IntegerProperty columnNumberProperty = new SimpleIntegerProperty();
    
    private EditorSetController editorSetController;
    
    public StatusModelImpl(EditorSetController editorSetController) {
        this.editorSetController = editorSetController;
        editorSetController.getObservable().registerObserver(this);
        lineNumberProperty.addListener((observable, oldLineNumber, newLineNumber) -> notifyObservers());
        columnNumberProperty.addListener((observable, oldColumnNumber, newColumnNumber) -> notifyObservers());
    }

    @Override
    public void update() {
        EditorModel activeModel = editorSetController.getActiveEditorModel();
        lineNumberProperty.bind(activeModel.lineNumberProperty());
        columnNumberProperty.bind(activeModel.columnNumberProperty());
        notifyObservers();
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
}
