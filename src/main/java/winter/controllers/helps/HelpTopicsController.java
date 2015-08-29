package winter.controllers.helps;

import winter.models.helps.HelpTopicModel;
import winter.views.help.HelpTopicsView;

/**
 * Created by melvic on 8/29/15.
 */
public interface HelpTopicsController {
    public void showHelpTopics();

    public HelpTopicsView getHelpTopicsView();

    public HelpTopicModel getHelpTopicModel();
}
