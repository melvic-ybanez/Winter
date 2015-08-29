package winter.factories;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import winter.models.helps.HelpTopicModel;
import winter.models.helps.HelpTopicModelImpl;
import winter.utils.Either;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by melvic on 8/29/15.
 */
public class HelpTopicsFactory {
    public static Either<Exception, HelpTopicModel> createHelpTopicFromJSON() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("help_topics.json"));
            JSONObject json = (JSONObject) obj;
            return Either.right(createHelpTopicFromJSON(json));
        } catch (IOException | ParseException e) {
            return Either.left(e);
        }
    }

    public static HelpTopicModel createHelpTopicFromJSON(JSONObject json) {
        String title = (String) json.getOrDefault("Title", "");
        String description = (String) json.getOrDefault("Description", "");

        List<String> instructions = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) json.getOrDefault("Instructions", new JSONArray());
        Iterator i = jsonArray.iterator();

        while (i.hasNext()) {
            String instruction = (String) i.next();
            instructions.add(instruction);
        }

        List<HelpTopicModel> subTopics = new ArrayList<>();
        jsonArray = (JSONArray) json.getOrDefault("Topics", new JSONArray());
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
