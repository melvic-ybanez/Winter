package winter.models.preferences;

import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by ybamelcash on 8/23/2015.
 */
public interface PreferencesView {
    public void populateWithData();

    public Optional<ButtonType> showAndWait();
}
