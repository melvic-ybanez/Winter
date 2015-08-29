package winter.factories;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import winter.models.helps.HelpTopicModel;
import winter.models.helps.HelpTopicModelImpl;
import winter.utils.Errors;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by melvic on 8/29/15.
 */
public class HelpTopicsFactory {
    public static Optional<HelpTopicModel> createHelpTopicFromJSON() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("hep_topics.json"));
            JSONObject json = (JSONObject) obj;
            return Optional.of(createHelpTopicFromJSON(json));
        } catch (IOException | ParseException e) {
            Errors.exceptionDialog("JSON Read Error",
                    "An error occured while reading json", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public static HelpTopicModel createHelpTopicFromJSON(JSONObject json) {
        String title = (String) json.get("Title");
        String description = (String) json.get("Description");

        List<String> instructions = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) json.get("Instructions");
        Iterator i = jsonArray.iterator();

        while (i.hasNext()) {
            String instruction = (String) i.next();
            instructions.add(instruction);
        }

        List<HelpTopicModel> subTopics = new ArrayList<>();
        jsonArray = (JSONArray) json.get("Topics");
        i = jsonArray.iterator();

        while (i.hasNext()) {
            JSONObject subJson = (JSONObject) i.next();
            HelpTopicModel subTopic = createHelpTopicFromJSON(subJson);
            subTopics.add(subTopic);
        }

        HelpTopicModel topic = new HelpTopicModelImpl();
        topic.setTitle(title);
        topic.setDescription(description);
        topic.setInstructions(instructions);
        topic.setSubTopics(subTopics);

        return topic;
    }
}
