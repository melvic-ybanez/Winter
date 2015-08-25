package winter.models.preferences;

import javafx.beans.property.BooleanProperty;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public interface GeneralPrefModel extends PreferencesModel {
    public int getDefaultTabSpaceCount();

    public void setTabSpaceCount(int tabSpaceCount);

    public int getTabSpaceCount();

    public boolean getDefaultSaveFilesBeforeExit();

    public void setSaveFilesBeforeExit(boolean saveFilesBeforeExit);

    public boolean saveFilesBeforeExit();

    public String getTabString();

    public BooleanProperty removeExtraSpacesProperty();

    public void setRemoveExtraSpaces(boolean removeExtraSpaces);

    public boolean removeExtraSpaces();

    public boolean getDefaultRemoveExtraSpaces();
}
