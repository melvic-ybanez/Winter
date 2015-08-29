package winter.views.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Window;
import winter.controllers.helps.AboutController;
import winter.controllers.helps.AboutControllerImpl;
import winter.controllers.helps.HelpTopicsController;
import winter.controllers.helps.HelpTopicsControllerImpl;
import winter.factories.HelpTopicsFactory;
import winter.factories.Icons;
import winter.models.helps.AboutModel;
import winter.models.helps.AboutModelImpl;
import winter.models.helps.HelpTopicModel;
import winter.utils.Either;
import winter.utils.Errors;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class HelpMenu extends Menu {
    private MenuItem aboutItem;
    private MenuItem helpTopicsItem;
    private AboutController aboutController;
    private HelpTopicsController helpTopicsController;
    private Window window;

    public HelpMenu(Window window) {
        super("Help");

        this.window = window;

        AboutModel aboutModel = new AboutModelImpl();
        this.aboutController = new AboutControllerImpl(aboutModel, window);

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

        helpTopicsItem.setOnAction(e -> {
            if (helpTopicsController == null) {
                Either<Exception, HelpTopicModel> helpTopicModelEither = HelpTopicsFactory.createHelpTopicFromJSON();
                helpTopicModelEither.ifLeft(ex -> {
                    Errors.exceptionDialog("JSON Read Error",
                            "An error occured while reading json", ex.getMessage(), ex);
                });
                helpTopicModelEither.ifRight(helpTopicModel -> {
                    helpTopicsController = new HelpTopicsControllerImpl(helpTopicModel, window);
                });
            }
            if (helpTopicsController != null) {
                helpTopicsController.showHelpTopics();
            }
        });
    }

    public MenuItem getHelpTopicsItem() {
        return helpTopicsItem;
    }
}
