package winter.models.preferences;

import winter.utils.SimpleObservable;
import winter.utils.StringUtils;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public class GeneralPrefModelImpl extends SimpleObservable implements GeneralPrefModel {
    private int tabSpaceCount;
    private boolean saveFilesBeforeExit;

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
        notifyObservers();
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
        notifyObservers();
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
    public void reset() {
        setTabSpaceCount(getDefaultTabSpaceCount());
        setSaveFilesBeforeExit(getDefaultSaveFilesBeforeExit());
    }
}
