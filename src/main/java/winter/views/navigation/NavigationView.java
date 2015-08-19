package winter.views.navigation;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import winter.controllers.editors.EditorSetController;
import winter.controllers.navigations.NavigationController;
import winter.models.editors.EditorModel;
import winter.utils.StreamUtils;
import winter.views.editor.EditorView;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 8/19/2015.
 */
public class NavigationView extends Stage {
    private NavigationController navigationController;

    private TextField filenameField = new TextField();
    private ListView<EditorModel> filesView = new ListView<>();

    public NavigationView(String defaultText, NavigationController navigationController, EditorSetController editorSetController) {
        setNavigationController(navigationController);
        filenameField.setText(defaultText);

        BorderPane mainPane = new BorderPane();
        mainPane.setTop(filenameField);
        mainPane.setBottom(filesView);

        Scene scene = new Scene(mainPane);
        EditorView editorView = editorSetController.getActiveEditorView();

        setScene(scene);
        initStyle(StageStyle.UNDECORATED);
        initOwner(editorView.getScene().getWindow());
        show();

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });

        filenameField.setOnKeyReleased(event -> navigationController.filenameAutoCompleteOnType());
        filenameField.setOnAction(e -> navigationController.selectFilename());

        filesView.setCellFactory(listView -> new ListCell<EditorModel>() {
            @Override
            public void updateItem(EditorModel editorModel, boolean empty) {
                super.updateItem(editorModel, empty);

                if (editorModel == null) {
                    setGraphic(null);
                    return;
                }

                String title = editorModel.getTitle();
                VBox pane = new VBox();

                Label titleLabel = new Label(title);
                Label pathLabel = new Label();

                int titleSize = 13;
                String titleDefaultStyle = "-fx-font-size: " + titleSize + ";";

                titleLabel.setStyle(titleDefaultStyle);
                if (getIndex() == 0) {
                    titleLabel.setStyle(titleDefaultStyle + "-fx-font-weight: bold");
                }
                pathLabel.setStyle("-fx-font-style: italic; " +
                        "-fx-font-size: " + (titleSize - 1) + "; " +
                        "-fx-text-fill: gray");

                if (editorModel.isUntitled()) {
                    pathLabel.setText("Path not available");
                } else {
                    Path path = editorModel.getPath().get();
                    pathLabel.setText(path.toString());
                }

                pane.getChildren().addAll(titleLabel, pathLabel);
                setGraphic(pane);
            }
        });
    }

    public NavigationController getNavigationController() {
        return navigationController;
    }

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public TextField getFilenameField() {
        return filenameField;
    }

    public void setFilenameField(TextField filenameField) {
        this.filenameField = filenameField;
    }

    public ListView<EditorModel> getFilesView() {
        return filesView;
    }

    public void setFilesView(ListView<EditorModel> filesView) {
        this.filesView = filesView;
    }
}
