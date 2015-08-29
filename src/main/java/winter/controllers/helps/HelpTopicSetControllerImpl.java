package winter.controllers.helps;

import javafx.stage.Window;
import winter.models.helps.HelpTopicModel;
import winter.views.help.HelpTopicSetView;
import winter.views.help.HelpTopicView;

/**
 * Created by melvic on 8/29/15.
 */
public class HelpTopicSetControllerImpl implements HelpTopicSetController {
    private HelpTopicModel helpTopicModel;
    private HelpTopicSetView helpTopicSetView;
    private Window window;

    public HelpTopicSetControllerImpl(HelpTopicModel helpTopicModel, Window window) {
        this.helpTopicModel = helpTopicModel;
        this.window = window;
    }

    @Override
    public void showHelpTopicSetView() {
        if (helpTopicSetView == null) {
            helpTopicSetView = new HelpTopicSetView(helpTopicModel, this, window);
        }
        helpTopicSetView.showAndWait();
    }

    @Override
    public void showHelpTopic(HelpTopicModel helpTopicModel) {
        HelpTopicView helpTopicView = helpTopicSetView.getHelpTopicView();
        helpTopicView.setHelpTopicModel(helpTopicModel);
    }

    @Override
    public HelpTopicSetView getHelpTopicSetView() {
        return helpTopicSetView;
    }

    @Override
    public HelpTopicModel getHelpTopicModel() {
        return helpTopicModel;
    }
}
