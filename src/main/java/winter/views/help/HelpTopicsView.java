package winter.views.help;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import winter.models.helps.HelpTopicModel;

/**
 * Created by melvic on 8/29/15.
 */
public class HelpTopicsView extends Stage {
    private HelpTopicModel helpTopicModel;
    private TreeView<String> tree;
    private TextArea area;

    public HelpTopicsView(HelpTopicModel helpTopicModel, Window window) {
        setTitle("Help Topics");
        setHelpTopicModel(helpTopicModel);

        initOwner(window);
        initModality(Modality.APPLICATION_MODAL);
    }

    public void init() {
        tree = new TreeView<>();
        TreeItem<String> root = createTopicTreeItem(helpTopicModel);
        tree.setRoot(root);

        TitledPane topicsTreePane = new TitledPane();
        topicsTreePane.setText("Topics");
        topicsTreePane.setContent(tree);
        topicsTreePane.setExpanded(true);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(tree, area);

        Scene scene = new Scene(splitPane);
        scene.getStylesheets().add(HelpTopicsView.class.getResource("/styles/help.css").toExternalForm());
        setScene(scene);
    }

    private TreeItem<String> createTopicTreeItem(HelpTopicModel helpTopicModel) {
        TreeItem<String> node = new TreeItem<>(helpTopicModel.getTitle());
        helpTopicModel.getSubTopics().forEach(helpTopicModel1 -> {
            TreeItem<String> child = createTopicTreeItem(helpTopicModel);
            node.getChildren().add(child);
        });
        return node;
    }

    public HelpTopicModel getHelpTopicModel() {
        return helpTopicModel;
    }

    public void setHelpTopicModel(HelpTopicModel helpTopicModel) {
        this.helpTopicModel = helpTopicModel;
    }
}
