package winter.views.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Window;
import winter.factories.Icons;
import winter.views.help.AboutView;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class HelpMenu extends Menu {
    private MenuItem aboutItem;
    private MenuItem helpTopicsItem;
    private AboutView aboutView;
    private Window window;

    public HelpMenu(Window window) {
        super("Help");
        this.window = window;
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
        aboutItem.setOnAction(e -> {
            if (aboutView == null) {
                aboutView = new AboutView(window);
                aboutView.init();
            }
            aboutView.showAndWait();
        });
    }

    public MenuItem getHelpTopicsItem() {
        return helpTopicsItem;
    }
}
