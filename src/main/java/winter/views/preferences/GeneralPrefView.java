package winter.views.preferences;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import winter.controllers.preferences.GeneralPrefController;
import winter.models.preferences.GeneralPrefModel;

import java.util.Optional;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public class GeneralPrefView extends Dialog<ButtonType> implements PreferencesView {
    private TextField spaceCountField;
    private CheckBox saveFilesBeforeExitBox;
    private CheckBox removeExtraSpacesBox;
    private CheckBox wrapTextBox;
    private Hyperlink fontSettingsLink;
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
        removeExtraSpacesBox = new CheckBox("Remove extra lines and spaces from file contents.");
        wrapTextBox = new CheckBox("Wrap Text.");
        fontSettingsLink = new Hyperlink("Change Font Settings");
        resetButton = new Button("Reset to Defaults");

        spaceCountField.setPrefColumnCount(4);

        HBox spacesPane = new HBox();
        Label spaceCountLabel = new Label("Number of spaces per indentations:");
        spaceCountLabel.setTranslateY(5);
        spacesPane.getChildren().addAll(spaceCountLabel, spaceCountField);
        spacesPane.setSpacing(10);

        VBox centerPane = new VBox();
        centerPane.getChildren().addAll(spacesPane, saveFilesBeforeExitBox, removeExtraSpacesBox, wrapTextBox, fontSettingsLink);
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
        fontSettingsLink.setOnAction(e -> generalPrefController.changeFontSettings());
    }

    public void populateWithData() {
        spaceCountField.setText(generalPrefModel.getTabSpaceCount() + "");
        saveFilesBeforeExitBox.setSelected(generalPrefModel.saveFilesBeforeExit());
        removeExtraSpacesBox.setSelected(generalPrefModel.removeExtraSpaces());
        wrapTextBox.setSelected(generalPrefModel.wrapText());
    }

    @Override
    public Optional<ButtonType> showAndGetResult() {
        return showAndWait();
    }

    public TextField getSpaceCountField() {
        return spaceCountField;
    }

    public CheckBox getSaveFilesBeforeExitBox() {
        return saveFilesBeforeExitBox;
    }

    public CheckBox getRemoveExtraSpacesBox() {
        return removeExtraSpacesBox;
    }

    public CheckBox getWrapTextBox() {
        return wrapTextBox;
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
