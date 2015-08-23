package winter.views.preferences;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.fxmisc.richtext.CodeArea;
import winter.controllers.preferences.FontPrefController;
import winter.models.preferences.FontPrefModel;
import winter.models.preferences.PreferencesView;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public class FontPrefView extends Dialog<ButtonType> implements PreferencesView {
    private ComboBox<String> fontFamilyCombo;
    private ComboBox<String> fontStyleCombo;
    private ComboBox<Integer> fontSizeCombo;
    private CodeArea sampleEditor;
    private Button resetButton;

    private FontPrefController fontPrefController;
    private FontPrefModel fontPrefModel;

    public FontPrefView(FontPrefController fontPrefController, FontPrefModel fontPrefModel) {
        super();
        setFontPrefController(fontPrefController);
        setFontPrefModel(fontPrefModel);

        setTitle("Font Settings");
        init();
    }

    private void init() {
        fontFamilyCombo = new ComboBox<>();
        fontStyleCombo = new ComboBox<>();
        fontSizeCombo = new ComboBox<>();
        sampleEditor = new CodeArea();
        resetButton = new Button("Reset to Defaults");

        fontFamilyCombo.setEditable(true);
        fontStyleCombo.setEditable(true);
        fontStyleCombo.setPrefWidth(90);
        fontSizeCombo.setEditable(true);
        fontSizeCombo.setPrefWidth(60);

        sampleEditor.getStyleClass().add("sample-code-area");
        int space = 10;

        HBox fontFamilyPane = new HBox();
        fontFamilyPane.getChildren().addAll(createFontLabel("Font Family"), fontFamilyCombo);
        fontFamilyPane.setSpacing(space);

        HBox fontStylePane = new HBox();
        fontStylePane.getChildren().addAll(createFontLabel("Font Style"), fontStyleCombo);
        fontStylePane.setSpacing(space);

        HBox fontSizePane = new HBox();
        fontSizePane.getChildren().addAll(createFontLabel("Font Size"), fontSizeCombo);
        fontSizePane.setSpacing(space);

        HBox fontPane = new HBox();
        fontPane.getChildren().addAll(fontFamilyPane, fontStylePane, fontSizePane);
        fontPane.setSpacing(space * 3);

        BorderPane resetPane = new BorderPane();
        resetPane.setRight(resetButton);

        BorderPane centerPane = new BorderPane();
        centerPane.setCenter(sampleEditor);
        centerPane.setPadding(new Insets(20, 0, 20, 0));

        BorderPane mainPane = new BorderPane();
        mainPane.setTop(fontPane);
        mainPane.setCenter(centerPane);
        mainPane.setBottom(resetPane);

        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
        getDialogPane().setContent(mainPane);

        getDialogPane().getScene().getStylesheets().add(FontPrefView.class.getResource("/styles/preferences.css").toExternalForm());
    }

    private Label createFontLabel(String text) {
        Label label = new Label(text + ":");
        label.setTranslateY(5);
        return label;
    }

    public void registerEvents() {
        resetButton.setOnAction(e -> fontPrefController.resetToDefaults());
        Runnable fontChangeEvent = fontPrefController::fontChanged;
        fontFamilyCombo.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> fontChangeEvent.run());
        fontStyleCombo.getSelectionModel().selectedItemProperty().addListener(event -> fontChangeEvent.run());
        fontSizeCombo.getSelectionModel().selectedItemProperty().addListener(event -> fontChangeEvent.run());
    }

    @Override
    public void populateWithData() {
        sampleEditor.replaceText(fontPrefModel.getSampleString());
        fontFamilyCombo.getSelectionModel().select(fontPrefModel.getFontFamily());
        fontStyleCombo.getSelectionModel().select(fontPrefModel.getFontStyle());
        fontSizeCombo.getSelectionModel().select(fontPrefModel.getFontSize());
    }

    public FontPrefController getFontPrefController() {
        return fontPrefController;
    }

    public void setFontPrefController(FontPrefController fontPrefController) {
        this.fontPrefController = fontPrefController;
    }

    public FontPrefModel getFontPrefModel() {
        return fontPrefModel;
    }

    public void setFontPrefModel(FontPrefModel fontPrefModel) {
        this.fontPrefModel = fontPrefModel;
    }

    public ComboBox<String> getFontFamilyCombo() {
        return fontFamilyCombo;
    }

    public ComboBox<String> getFontStyleCombo() {
        return fontStyleCombo;
    }

    public ComboBox<Integer> getFontSizeCombo() {
        return fontSizeCombo;
    }

    public CodeArea getSampleEditor() {
        return sampleEditor;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public String getFontFamily() {
        return fontFamilyCombo.getSelectionModel().getSelectedItem();
    }

    public String getFontStyle() {
        return fontStyleCombo.getSelectionModel().getSelectedItem();
    }

    public int getFontSize() {
        return fontSizeCombo.getSelectionModel().getSelectedItem();
    }
}
