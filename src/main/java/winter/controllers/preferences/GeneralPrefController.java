package winter.controllers.preferences;

import winter.views.preferences.GeneralView;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public interface GeneralPrefController {
    public void showUI();

    public void applySettings();

    public void resetDefault();

    public void setGeneralView(GeneralView generalView);

    public GeneralView getGeneralView();
}
