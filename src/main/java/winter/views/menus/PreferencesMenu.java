package winter.views.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import winter.controllers.preferences.FontPrefController;
import winter.controllers.preferences.GeneralPrefController;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class PreferencesMenu extends Menu {
    private MenuItem generalItem;
    private MenuItem fontItem;

    private GeneralPrefController generalPrefController;
    private FontPrefController fontPrefController;

    public PreferencesMenu(GeneralPrefController generalPrefController, FontPrefController fontPrefController) {
        super("Preferences");
        this.generalPrefController = generalPrefController;
        this.fontPrefController = fontPrefController;
        init();
        registerEvents();
    }

    private void init() {
        generalItem = new MenuItem("General Settings...");
        fontItem = new MenuItem("Change Fonts...");

        getItems().addAll(generalItem, fontItem);
    }

    private void registerEvents() {
        generalItem.setOnAction(e -> generalPrefController.showUI());
        fontItem.setOnAction(e -> fontPrefController.showUI());
    }

    public GeneralPrefController getGeneralPrefController() {
        return generalPrefController;
    }

    public FontPrefController getFontPrefController() {
        return fontPrefController;
    }
}
