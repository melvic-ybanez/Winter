package winter.views.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class PreferencesMenu extends Menu {
    private MenuItem generalItem;
    private MenuItem fontItem;
    private MenuItem syntaxItem;

    public PreferencesMenu() {
        super("Preferences");
        init();
    }

    private void init() {
        generalItem = new MenuItem("General...");
        fontItem = new MenuItem("Font...");
        syntaxItem = new MenuItem("Language Syntax...");

        getItems().addAll(generalItem, fontItem, syntaxItem);
    }
}
