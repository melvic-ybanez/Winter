package winter.views.help;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import winter.controllers.helps.HelpTopicSetController;
import winter.models.helps.HelpTopicModel;

/**
 * Created by melvic on 8/29/15.
 */
public class HelpTopicSetView extends Stage {
    private HelpTopicModel helpTopicModel;
    private HelpTopicSetController helpTopicSetController;
    private TreeView<String> tree;
    private HelpTopicView helpTopicView;

    public HelpTopicSetView(HelpTopicModel helpTopicModel, HelpTopicSetController helpTopicSetController, Window window) {
        setTitle("Help Topics");
        setHelpTopicModel(helpTopicModel);
        setHelpTopicSetController(helpTopicSetController);

        initOwner(window);
        initModality(Modality.APPLICATION_MODAL);
    }

    public void init() {
        tree = new TreeView<>();
        helpTopicView = new HelpTopicView(helpTopicModel);
        TreeItem<String> root = createTopicTreeItem(helpTopicModel);
        tree.setRoot(root);
        root.setExpanded(true);

        TitledPane topicsTreePane = new TitledPane();
        topicsTreePane.setText("Topics");
        topicsTreePane.setContent(tree);
        topicsTreePane.setCollapsible(false);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(topicsTreePane, new ScrollPane(helpTopicView));
        splitPane.getStyleClass().add("winter-divider");
        splitPane.setDividerPosition(0, 0.4f);

        Scene scene = new Scene(splitPane);
        scene.getStylesheets().add(HelpTopicSetView.class.getResource("/styles/help.css").toExternalForm());
        scene.getStylesheets().add(HelpTopicSetView.class.getResource("/styles/meruem.css").toExternalForm());
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

    public HelpTopicSetController getHelpTopicSetController() {
        return helpTopicSetController;
    }

    public void setHelpTopicSetController(HelpTopicSetController helpTopicSetController) {
        this.helpTopicSetController = helpTopicSetController;
    }
}
