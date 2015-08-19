package winter.views.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import winter.controllers.navigations.NavigationController;
import winter.factories.Icons;

/**
 * Created by ybamelcash on 8/17/2015.
 */
public class NavigationMenu extends Menu {
    private MenuItem goToFile;
    private MenuItem goToLine;

    private NavigationController navigationController;

    public NavigationMenu(NavigationController navigationController) {
        super("Navigation");
        this.navigationController = navigationController;
        init();
    }

    private void init() {
        goToFile = new MenuItem("Go to File...", Icons.createFindFileIcon());
        goToLine = new MenuItem("Go to Line...");

        goToFile.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCodeCombination.CONTROL_DOWN));

        goToFile.setOnAction(e -> navigationController.showGoToFileUI());
        goToLine.setOnAction(e -> navigationController.goToLine());

        getItems().addAll(goToFile, goToLine);
    }
}
