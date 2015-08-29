package winter.views.help;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import winter.models.helps.HelpTopicModel;
import winter.utils.StreamUtils;

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

    public HelpTopicView(HelpTopicModel helpTopicModel, ReadOnlyDoubleProperty heightProperty) {
        setHelpTopicModel(helpTopicModel);
        init();
        prefHeightProperty().bind(heightProperty);
        getStyleClass().add("topic");
    }

    private void init() {
        titleLabel = new Label(helpTopicModel.getTitle());
        titleLabel.getStyleClass().add("title");

        descriptionLabel = new Label(helpTopicModel.getDescription());
        List<String> instructions = helpTopicModel.getInstructions();
        String instructionsString = "";
        if (!instructions.isEmpty()) {
            instructionsString = "Instructions: \n\n";
            List<String> instructs = StreamUtils.mapToList(IntStream.range(0, instructions.size()).boxed(), i -> {
                return "\t" + (i + 1) + ". " + instructions.get(i);
            });
            instructionsString += String.join("\n\n", instructs);
        }
        instructionsLabel = new Label(instructionsString);

        subTopics = StreamUtils.mapToList(helpTopicModel.getSubTopics().stream(),
                topic -> new Hyperlink(topic.getTitle()));

        BorderPane topPane = new BorderPane();
        BorderPane titlePane = new BorderPane();
        titlePane.setLeft(titleLabel);
        topPane.setCenter(titlePane);
        topPane.setBottom(new Separator(Orientation.HORIZONTAL));

        BorderPane subTopicsPane = new BorderPane();
        VBox subTopicsCenterPane = new VBox();
        subTopicsCenterPane.getChildren().addAll(subTopics);
        subTopicsCenterPane.setPadding(new Insets(5, 0, 0, 10));
        subTopicsPane.setTop(new Label("Read Next:"));
        subTopicsPane.setBottom(subTopicsCenterPane);

        VBox centerPane = new VBox();
        centerPane.setSpacing(20);
        centerPane.setPadding(new Insets(15));
        centerPane.getChildren().addAll(descriptionLabel, instructionsLabel, subTopicsPane);

        setTop(topPane);
        setCenter(centerPane);
    }

    public HelpTopicModel getHelpTopicModel() {
        return helpTopicModel;
    }

    public void setHelpTopicModel(HelpTopicModel helpTopicModel) {
        this.helpTopicModel = helpTopicModel;
    }
}
