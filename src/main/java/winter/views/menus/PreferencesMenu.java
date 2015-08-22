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
    private MenuItem syntaxItem;

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
        generalItem = new MenuItem("General...");
        fontItem = new MenuItem("Font...");
        syntaxItem = new MenuItem("Language Syntax...");

        getItems().addAll(generalItem, fontItem, syntaxItem);
    }

    private void registerEvents() {
        generalItem.setOnAction(e -> generalPrefController.showUI());
        fontItem.setOnAction(e -> fontPrefController.showUI());
    }
}
