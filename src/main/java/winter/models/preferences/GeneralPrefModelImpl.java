package winter.models.preferences;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import winter.utils.StringUtils;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public class GeneralPrefModelImpl implements GeneralPrefModel {
    private int tabSpaceCount;
    private boolean saveFilesBeforeExit;
    private BooleanProperty removeExtraSpacesProperty = new SimpleBooleanProperty();

    public GeneralPrefModelImpl() {
        reset();
    }

    @Override
    public int getDefaultTabSpaceCount() {
        return 2;
    }

    @Override
    public void setTabSpaceCount(int tabSpaceCount) {
        this.tabSpaceCount = tabSpaceCount;
    }

    @Override
    public int getTabSpaceCount() {
        return tabSpaceCount;
    }

    @Override
    public boolean getDefaultSaveFilesBeforeExit() {
        return true;
    }

    @Override
    public void setSaveFilesBeforeExit(boolean saveFilesBeforeExit) {
        this.saveFilesBeforeExit = saveFilesBeforeExit;
    }

    @Override
    public boolean saveFilesBeforeExit() {
        return saveFilesBeforeExit;
    }

    @Override
    public String getTabString() {
        return StringUtils.repeat(getTabSpaceCount(), " ");
    }

    @Override
    public BooleanProperty removeExtraSpacesProperty() {
        return removeExtraSpacesProperty;
    }

    @Override
    public void setRemoveExtraSpaces(boolean removeExtraSpaces) {
        removeExtraSpacesProperty.set(removeExtraSpaces);
    }

    @Override
    public boolean removeExtraSpaces() {
        return removeExtraSpacesProperty.get();
    }

    @Override
    public boolean getDefaultRemoveExtraSpaces() {
        return false;
    }

    @Override
    public void reset() {
        setTabSpaceCount(getDefaultTabSpaceCount());
        setSaveFilesBeforeExit(getDefaultSaveFilesBeforeExit());
        setRemoveExtraSpaces(getDefaultRemoveExtraSpaces());
    }
}
