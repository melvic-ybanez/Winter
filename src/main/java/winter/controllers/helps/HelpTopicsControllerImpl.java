package winter.controllers.helps;

import javafx.stage.Window;
import winter.models.helps.HelpTopicModel;
import winter.views.help.HelpTopicsView;

/**
 * Created by melvic on 8/29/15.
 */
public class HelpTopicsControllerImpl implements HelpTopicsController {
    private HelpTopicModel helpTopicModel;
    private HelpTopicsView helpTopicsView;
    private Window window;

    public HelpTopicsControllerImpl(HelpTopicModel helpTopicModel, Window window) {
        this.helpTopicModel = helpTopicModel;
        this.window = window;
    }

    @Override
    public void showHelpTopics() {
        if (helpTopicsView == null) {
            helpTopicsView = new HelpTopicsView(helpTopicModel, this, window);
            helpTopicsView.init();
        }
        helpTopicsView.showAndWait();
    }

    @Override
    public HelpTopicsView getHelpTopicsView() {
        return helpTopicsView;
    }

    @Override
    public HelpTopicModel getHelpTopicModel() {
        return helpTopicModel;
    }
}
