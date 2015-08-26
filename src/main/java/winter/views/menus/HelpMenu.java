package winter.views.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import winter.controllers.helps.AboutController;
import winter.factories.Icons;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class HelpMenu extends Menu {
    private MenuItem aboutItem;
    private MenuItem helpTopicsItem;
    private AboutController aboutController;

    public HelpMenu(AboutController aboutController) {
        super("Help");
        this.aboutController = aboutController;
        init();
        registerEvents();
    }

    private void init() {
        aboutItem = new MenuItem("About");

        helpTopicsItem = new MenuItem("Help Topics");
        helpTopicsItem.setGraphic(Icons.createHelpIcon());
        helpTopicsItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCodeCombination.CONTROL_DOWN,
                KeyCodeCombination.SHIFT_DOWN));

        getItems().addAll(helpTopicsItem, aboutItem);
    }

    private void registerEvents() {
        aboutItem.setOnAction(e -> aboutController.showUI());
    }

    public MenuItem getHelpTopicsItem() {
        return helpTopicsItem;
    }
}
