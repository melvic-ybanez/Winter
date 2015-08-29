package winter.views.help;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import winter.models.helps.HelpTopicModel;
import winter.utils.StreamUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by melvic on 8/30/15.
 */
public class HelpTopicView extends BorderPane {
    private HelpTopicModel helpTopicModel;
    private Label titleLabel;
    private Label descriptionLabel;
    private Label instructionsLabel;
    private List<Hyperlink> subTopics;

    public HelpTopicView(HelpTopicModel helpTopicModel) {
        init();
        setHelpTopicModel(helpTopicModel);
        getStyleClass().add("topic");
    }

    private void init() {
        titleLabel = new Label();
        titleLabel.getStyleClass().add("title");

        descriptionLabel = new Label();
        instructionsLabel = new Label();
        subTopics = new ArrayList<>();

        BorderPane topPane = new BorderPane();
        BorderPane titlePane = new BorderPane();
        titlePane.setLeft(titleLabel);
        topPane.setCenter(titlePane);
        topPane.setBottom(new Separator(Orientation.HORIZONTAL));

        VBox centerPane = new VBox();
        centerPane.setSpacing(20);
        centerPane.setPadding(new Insets(15));
        centerPane.getChildren().addAll(descriptionLabel, instructionsLabel);
        centerPane.getChildren().addAll(subTopics);

        setTop(topPane);
        setCenter(centerPane);
    }

    public HelpTopicModel getHelpTopicModel() {
        return helpTopicModel;
    }

    public void setHelpTopicModel(HelpTopicModel helpTopicModel) {
        this.helpTopicModel = helpTopicModel;
        titleLabel.setText(helpTopicModel.getTitle());
        descriptionLabel.setText(helpTopicModel.getDescription());

        List<String> instructions = helpTopicModel.getInstructions();
        String instructionsString = "";
        if (!instructions.isEmpty()) {
            instructionsString = "Instructions: \n\n";
            List<String> intructs = StreamUtils.mapToList(IntStream.range(0, instructions.size()).boxed(), i -> {
                return "\t" + (i + 1) + ". " + instructions.get(i);
            });
            instructionsString += String.join("\n\n", intructs);
        }
        instructionsLabel.setText(instructionsString);

        subTopics = StreamUtils.mapToList(helpTopicModel.getSubTopics().stream(),
                topic -> new Hyperlink(topic.getTitle()));
    }
}
