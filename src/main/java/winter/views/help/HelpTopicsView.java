package winter.views.help;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import winter.controllers.helps.HelpTopicsController;
import winter.models.helps.HelpTopicModel;

/**
 * Created by melvic on 8/29/15.
 */
public class HelpTopicsView extends Stage {
    private HelpTopicModel helpTopicModel;
    private HelpTopicsController helpTopicsController;
    private TreeView<String> tree;
    private TextArea area;

    public HelpTopicsView(HelpTopicModel helpTopicModel, HelpTopicsController helpTopicsController, Window window) {
        setTitle("Help Topics");
        setHelpTopicModel(helpTopicModel);
        setHelpTopicsController(helpTopicsController);

        initOwner(window);
        initModality(Modality.APPLICATION_MODAL);
    }

    public void init() {
        tree = new TreeView<>();
        area = new TextArea();
        TreeItem<String> root = createTopicTreeItem(helpTopicModel);
        tree.setRoot(root);
        root.setExpanded(true);

        TitledPane topicsTreePane = new TitledPane();
        topicsTreePane.setText("Topics");
        topicsTreePane.setContent(tree);
        topicsTreePane.setCollapsible(false);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(topicsTreePane, area);
        splitPane.getStyleClass().add("winter-divider");
        splitPane.setDividerPosition(0, 0.4f);

        Scene scene = new Scene(splitPane);
        scene.getStylesheets().add(HelpTopicsView.class.getResource("/styles/help.css").toExternalForm());
        scene.getStylesheets().add(HelpTopicsView.class.getResource("/styles/meruem.css").toExternalForm());
        setScene(scene);
    }

    private TreeItem<String> createTopicTreeItem(HelpTopicModel helpTopicModel) {
        TreeItem<String> node = new TreeItem<>(helpTopicModel.getTitle());
        helpTopicModel.getSubTopics().forEach(subTopic -> {
            TreeItem<String> child = createTopicTreeItem(subTopic);
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

    public HelpTopicsController getHelpTopicsController() {
        return helpTopicsController;
    }

    public void setHelpTopicsController(HelpTopicsController helpTopicsController) {
        this.helpTopicsController = helpTopicsController;
    }
}
