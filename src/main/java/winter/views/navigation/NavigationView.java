package winter.views.navigation;

import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.function.Consumer;

/**
 * Created by ybamelcash on 8/19/2015.
 */
public class NavigationView extends Stage {
    private NavigationController navigationController;

    private BorderPane mainPane = new BorderPane();
    private TextField filenameField;
    private ListView<EditorModel> filesView;

    public NavigationView(NavigationController navigationController, EditorSetController editorSetController) {
        setNavigationController(navigationController);

        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(NavigationView.class.getResource("/styles/goto-file.css").toExternalForm());
        EditorView editorView = editorSetController.getActiveEditorView();

        setScene(scene);
        initStyle(StageStyle.UNDECORATED);
        initOwner(editorView.getScene().getWindow());

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });
    }

    public void recreateUI() {
        filenameField = new TextField();
        filesView = new ListView<>();

        mainPane.setTop(filenameField);
        mainPane.setBottom(filesView);

        filenameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                filesView.requestFocus();
                filesView.getSelectionModel().select(0);
            }
        });
        filenameField.textProperty().addListener(event -> navigationController.filenameAutoCompleteOnType());
        filenameField.setOnAction(e -> navigationController.selectFilename());

        filesView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Consumer<EditorModel> editorModelConsumer = navigationController::selectFilename;
        filesView.setCellFactory(listView -> new ListCell<EditorModel>() {
            @Override
            public void updateItem(EditorModel editorModel, boolean empty) {
                super.updateItem(editorModel, empty);

                if (editorModel == null) {
                    setGraphic(null);
                    setOnMouseClicked(null);
                    return;
                }

                String title = editorModel.getTitle();
                VBox pane = new VBox();

                Label titleLabel = new Label(title);
                Label pathLabel = new Label();

                String titleClass = "list-title";
                if (getIndex() == 0) {
                    titleClass = "list-selected-title";
                }
                titleLabel.getStyleClass().add(titleClass);

                pathLabel.getStyleClass().add("list-path-label");

                if (editorModel.isUntitled()) {
                    pathLabel.setText("Path not available");
                } else {
                    Path path = editorModel.getPath().get();
                    pathLabel.setText(path.toString());
                }

                pane.getChildren().addAll(titleLabel, pathLabel);
                setGraphic(pane);
                setOnMouseClicked(event -> editorModelConsumer.accept(editorModel));
            }
        });
        filesView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                EditorModel editorModel = filesView.getSelectionModel().getSelectedItem();
                editorModelConsumer.accept(editorModel);
            } else if (event.getCode() == KeyCode.ESCAPE) {
                close();
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
