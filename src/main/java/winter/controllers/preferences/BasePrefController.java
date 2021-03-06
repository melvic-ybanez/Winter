package winter.controllers.preferences;

import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by ybamelcash on 8/23/2015.
 */
public abstract class BasePrefController implements PreferencesController {
    @Override
    public void showUI() {
        initPreferencesView();
        showAndHandleResult();
    }

    @Override
    public void resetToDefaults() {
        getModel().reset();
        getView().populateWithData();
    }

    @Override
    public void showAndHandleResult() {
        Optional<ButtonType> result = getView().showAndGetResult();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.APPLY) {
                applySettings();
            } else if (buttonType == ButtonType.CANCEL) {
                getView().populateWithData();
            }
        });
    }
}
