package winter.models.preferences;

import winter.utils.Observable;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public interface GeneralPrefModel {
    public int getDefaultTabSpaceCount();

    public void setTabSpaceCount(int tabSpaceCount);

    public int getTabSpaceCount();

    public boolean getDefaultSaveFilesBeforeExit();

    public void setSaveFilesBeforeExit(boolean saveFilesBeforeExit);

    public boolean saveFilesBeforeExit();

    public String getTabString();

    public void reset();
}
