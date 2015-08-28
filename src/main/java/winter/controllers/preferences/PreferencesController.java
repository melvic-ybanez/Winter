package winter.controllers.preferences;

import winter.models.preferences.PreferencesModel;
import winter.views.preferences.PreferencesView;

/**
 * Created by ybamelcash on 8/23/2015.
 */
public interface PreferencesController {
    public void showUI();

    public void applySettings();

    public void resetToDefaults();

    public PreferencesView getView();

    public PreferencesModel getModel();

    public void initPreferencesView();

    public void showAndHandleResult();
}
