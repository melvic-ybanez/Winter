package winter.controllers.helps;

import winter.models.helps.HelpTopicModel;
import winter.views.help.HelpTopicSetView;

/**
 * Created by melvic on 8/29/15.
 */
public interface HelpTopicSetController {
    public void showHelpTopicSetView();

    public void showHelpTopic(HelpTopicModel helpTopicModel);

    public HelpTopicSetView getHelpTopicSetView();

    public HelpTopicModel getHelpTopicModel();
}
