package winter.controllers.helps;

import java.util.List;

/**
 * Created by melvic on 8/28/15.
 */
public class HelpTopicModelImpl implements HelpTopicModel {
    private String title;
    private String description;
    private List<String> instructions;
    private List<HelpTopicModel> subTopics;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public List<HelpTopicModel> getSubTopics() {
        return subTopics;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public void setSubTopics(List<HelpTopicModel> subTopics) {
        this.subTopics = subTopics;
    }
}
