package winter.controllers.preferences;

import winter.views.preferences.GeneralPrefView;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public interface GeneralPrefController {
    public void showUI();

    public void applySettings();

    public void resetToDefaults();

    public void setGeneralPrefView(GeneralPrefView generalPrefView);

    public GeneralPrefView getGeneralPrefView();
}
