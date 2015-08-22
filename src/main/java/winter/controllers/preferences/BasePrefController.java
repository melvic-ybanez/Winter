package winter.controllers.preferences;

import javafx.scene.control.ButtonType;
import winter.models.preferences.PreferencesModel;
import winter.models.preferences.PreferencesView;

import java.util.Optional;

/**
 * Created by ybamelcash on 8/23/2015.
 */
public abstract class BasePrefController implements PreferencesController {
    @Override
    public void showUI() {
        if (getView() == null) {
            initPreferencesView();
        }
        handleResult();
    }

    @Override
    public void resetToDefaults() {
        getModel().reset();
        getView().populateWithData();
    }

    @Override
    public void handleResult() {
        Optional<ButtonType> result = getView().showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.APPLY) {
                applySettings();
            } else if (buttonType == ButtonType.CANCEL) {
                getView().populateWithData();
            }
        });
    }
}
