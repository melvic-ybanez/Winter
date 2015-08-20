package winter.controllers.preferences;

import winter.views.preferences.GeneralView;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public class GeneralPrefControllerImpl implements GeneralPrefController {
    private GeneralView generalView;

    public GeneralPrefControllerImpl() {
        setGeneralView(new GeneralView());
    }

    @Override
    public void showUI() {
        generalView.showAndWait();
    }

    @Override
    public void applySettings() {

    }

    @Override
    public void resetDefault() {

    }

    @Override
    public void setGeneralView(GeneralView generalView) {
        this.generalView = generalView;
    }

    @Override
    public GeneralView getGeneralView() {
        return null;
    }
}
