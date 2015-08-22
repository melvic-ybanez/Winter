package winter.views.preferences;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import winter.controllers.preferences.GeneralPrefController;
import winter.models.preferences.GeneralPrefModel;
import winter.utils.Observer;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public class GeneralPrefView extends Dialog<ButtonType> {
    private TextField spaceCountField;
    private CheckBox saveFilesBeforeExitBox;
    private Button resetButton;

    private GeneralPrefController generalPrefController;
    private GeneralPrefModel generalPrefModel;

    public GeneralPrefView(GeneralPrefController generalPrefController, GeneralPrefModel generalPrefModel) {
        super();
        setGeneralPrefController(generalPrefController);
        setGeneralPrefModel(generalPrefModel);

        setTitle("General Settings");
        init();
        registerEvents();
    }

    private void init() {
        spaceCountField = new TextField();
        saveFilesBeforeExitBox = new CheckBox("Prompt to save files before exiting.");
        resetButton = new Button("Reset to Defaults");

        spaceCountField.setPrefColumnCount(4);

        HBox spacesPane = new HBox();
        spacesPane.getChildren().addAll(new Label("Number of spaces per indentations:"), spaceCountField);
        spacesPane.setSpacing(10);

        VBox centerPane = new VBox();
        centerPane.getChildren().addAll(spacesPane, saveFilesBeforeExitBox);
        centerPane.setSpacing(20);
        centerPane.setPadding(new Insets(0, 0, 50, 0));

        BorderPane southPane = new BorderPane();
        southPane.setRight(resetButton);
        southPane.setPadding(new Insets(0, 0, 10, 0));

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(centerPane);
        mainPane.setBottom(southPane);

        getDialogPane().setContent(mainPane);
        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
    }

    private void registerEvents() {
        resetButton.setOnAction(e -> generalPrefController.resetToDefaults());
    }

    public void populateWithData() {
        spaceCountField.setText(generalPrefModel.getTabSpaceCount() + "");
        saveFilesBeforeExitBox.setSelected(generalPrefModel.saveFilesBeforeExit());
    }

    public TextField getSpaceCountField() {
        return spaceCountField;
    }

    public CheckBox getSaveFilesBeforeExitBox() {
        return saveFilesBeforeExitBox;
    }

    public GeneralPrefController getGeneralPrefController() {
        return generalPrefController;
    }

    public void setGeneralPrefController(GeneralPrefController generalPrefController) {
        this.generalPrefController = generalPrefController;
    }

    public GeneralPrefModel getGeneralPrefModel() {
        return generalPrefModel;
    }

    public void setGeneralPrefModel(GeneralPrefModel generalPrefModel) {
        this.generalPrefModel = generalPrefModel;
    }
}
