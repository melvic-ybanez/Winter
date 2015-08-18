package winter.controllers.navigations;

import javafx.stage.Stage;

/**
 * Created by ybamelcash on 8/18/2015.
 */
public interface NavigationController {
    public void goToFile();

    public void goToLine();

    public Stage getGoToFilePopup();
}
