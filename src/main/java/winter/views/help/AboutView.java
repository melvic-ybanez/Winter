package winter.views.help;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import winter.Settings;
import winter.controllers.helps.AboutController;
import winter.models.helps.AboutModel;

import java.util.stream.IntStream;

/**
 * Created by ybamelcash on 8/26/2015.
 */
public class AboutView extends Stage {
    private Hyperlink projectNameLink;
    private Hyperlink authorLink;
    private Hyperlink iconsSourceLink;
    private Window window;

    private AboutController aboutController;
    private AboutModel aboutModel;

    public AboutView(AboutController aboutController, AboutModel aboutModel, Window window) {
        this.aboutController = aboutController;
        this.aboutModel = aboutModel;
        this.window = window;
    }

    public void init() {
        setTitle("About Winter");
        initOwner(window);
        initModality(Modality.WINDOW_MODAL);
        setResizable(false);

        projectNameLink = new Hyperlink(aboutModel.getProjectName());
        authorLink = new Hyperlink(aboutModel.getAuthor());
        iconsSourceLink = new Hyperlink(aboutModel.getIconsSourceURLString());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        String[] labels = { "Project Name", "Description", "Version", "Created by" };
        IntStream.range(0, labels.length).forEach(row -> {
            grid.add(createLabel(labels[row]), 0, row);
        });

        Node[] valueNodes = {
                projectNameLink, new Label(aboutModel.getDescription()),
                new Label(Settings.VERSION), authorLink
        };
        IntStream.range(0, valueNodes.length).forEach(row -> {
            grid.add(valueNodes[row], 2, row);
        });

        IntStream.range(0, labels.length).forEach(row -> grid.add(createLabel(":"), 1, row));

        GridPane bottomPane = new GridPane();
        bottomPane.add(createLabel("Icons downloaded from"), 0, 0);
        bottomPane.add(createLabel(":"), 1, 0);
        bottomPane.add(iconsSourceLink, 2, 0);
        bottomPane.setPadding(new Insets(10, 20, 20, 20));
        bottomPane.setHgap(grid.getHgap());

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(grid);
        mainPane.setBottom(bottomPane);

        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(AboutView.class.getResource("/styles/about.css").toExternalForm());
        setScene(scene);
    }

    public void registerEvents() {
        projectNameLink.setOnAction(e -> aboutController.openProjectWebPage());
        authorLink.setOnAction(e -> aboutController.openAuthorWebPage());
        iconsSourceLink.setOnAction(e -> aboutController.openIconsSourceWebPage());
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("about-label");
        return label;
    }
}
