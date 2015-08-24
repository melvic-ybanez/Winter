package winter.models.preferences;

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
}
