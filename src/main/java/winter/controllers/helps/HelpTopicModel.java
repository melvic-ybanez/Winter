package winter.controllers.helps;

import java.util.List;

/**
 * Created by melvic on 8/28/15.
 */
public interface HelpTopicModel {
    public String getTitle();

    public String getDescription();

    public List<String> getInstructions();

    public List<HelpTopicModel> getSubTopics();

    public void setTitle(String title);

    public void setDescription(String description);

    public void setInstructions(List<String> instructions);

    public void setSubTopics(List<HelpTopicModel> subTopics);
}
