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
    private TreeView<HelpTopicModel> tree;
    private HelpTopicView helpTopicView;
    private HelpTopicModel selectedTopic;

    public HelpTopicSetView(HelpTopicModel helpTopicModel, HelpTopicSetController helpTopicSetController, Window window) {
        setTitle("Help Topics");
        setHelpTopicModel(helpTopicModel);
        setHelpTopicSetController(helpTopicSetController);

        initOwner(window);
        initModality(Modality.APPLICATION_MODAL);

        init();
        registerEvents();

        tree.setCellFactory(treeView -> {
            return new TreeCell<HelpTopicModel>() {
                @Override
                public void updateItem(HelpTopicModel item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    if (isEmpty) {
                        setText(null);
                    } else {
                        setText(item.getTitle());
                    }
                }
            };
        });
    }

    private void init() {
        SplitPane splitPane = new SplitPane();
        tree = new TreeView<>();
        helpTopicView = new HelpTopicView(helpTopicModel, splitPane.heightProperty());
        TreeItem<HelpTopicModel> root = createTopicTreeItem(helpTopicModel);
        tree.setRoot(root);
        root.setExpanded(true);

        TitledPane topicsTreePane = new TitledPane();
        topicsTreePane.setText("Topics");
        topicsTreePane.setContent(tree);
        topicsTreePane.setCollapsible(false);

        splitPane.getItems().addAll(topicsTreePane, new ScrollPane(helpTopicView));
        splitPane.getStyleClass().add("winter-divider");
        splitPane.setDividerPosition(0, 0.4f);

        Scene scene = new Scene(splitPane);
        scene.getStylesheets().add(HelpTopicSetView.class.getResource("/styles/help.css").toExternalForm());
        scene.getStylesheets().add(HelpTopicSetView.class.getResource("/styles/meruem.css").toExternalForm());
        setScene(scene);
    }

    private void registerEvents() {
        tree.setOnMouseClicked(e -> {
            TreeItem<HelpTopicModel> item = tree.getSelectionModel().getSelectedItem();
            if (item == null) return;

            helpTopicSetController.showHelpTopic(item.getValue());
        });
    }

    private TreeItem<HelpTopicModel> createTopicTreeItem(HelpTopicModel helpTopicModel) {
        TreeItem<HelpTopicModel> node = new TreeItem<>(helpTopicModel);
        helpTopicModel.getSubTopics().forEach(subTopic -> {
            TreeItem<HelpTopicModel> child = createTopicTreeItem(subTopic);
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

    public HelpTopicView getHelpTopicView() {
        return helpTopicView;
    }
}
