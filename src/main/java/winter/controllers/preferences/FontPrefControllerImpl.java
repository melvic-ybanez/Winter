package winter.controllers.preferences;

import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import winter.controllers.editors.EditorSetController;
import winter.models.preferences.FontPrefModel;
import winter.models.preferences.PreferencesModel;
import winter.utils.Errors;
import winter.utils.StreamUtils;
import winter.views.preferences.FontPrefView;
import winter.views.preferences.PreferencesView;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public class FontPrefControllerImpl extends BasePrefController implements FontPrefController {
    private FontPrefModel fontPrefModel;
    private FontPrefView fontPrefView;
    private EditorSetController editorSetController;

    public FontPrefControllerImpl(FontPrefModel fontPrefModel, EditorSetController editorSetController) {
        setFontPrefModel(fontPrefModel);
        this.editorSetController = editorSetController;
        fontPrefModel.setSampleString("; Sample Text. You can edit this one.\n\n" +
                "The quick brown fox jump over the lazy dog");
        editorSetController.getEditorSetView().setFontStyleString(
                createFontStyleString(fontPrefModel.getFontFamily(),
                        fontPrefModel.getFontStyle(),
                        fontPrefModel.getFontSize()));
    }

    @Override
    public List<String> getSystemFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        return Arrays.asList(fontFamilies);
    }

    @Override
    public void fontChanged() {
        if (validateFontValues()) {
            fontPrefView.getSampleEditor().setStyle(createFontStyleString());
        }
    }

    @Override
    public void applySettings() {
        if (!validateFontValues()) {
            showUI();
            return;
        }

        fontPrefModel.setFontFamily(fontPrefView.getFontFamily());
        fontPrefModel.setFontStyle(fontPrefView.getFontStyle());
        fontPrefModel.setFontSize(fontPrefView.getFontSize());
        fontPrefModel.setSampleString(fontPrefView.getSampleEditor().getText());

        editorSetController.getEditorSetView().setFontStyleString(createFontStyleString());
    }

    @Override
    public PreferencesView getView() {
        return getFontPrefView();
    }

    @Override
    public PreferencesModel getModel() {
        return getFontPrefModel();
    }

    @Override
    public void initPreferencesView() {
        setFontPrefView(new FontPrefView(this, fontPrefModel));
        fontPrefView.getFontFamilyCombo().getItems().addAll(getSystemFonts());
        fontPrefView.getFontStyleCombo().getItems().addAll(getFontStyles());
        fontPrefView.getFontSizeCombo().getItems().addAll(getFontSizes());
        fontPrefView.populateWithData();
        fontPrefView.registerEvents();
    }

    @Override
    public FontPrefModel getFontPrefModel() {
        return fontPrefModel;
    }

    @Override
    public void setFontPrefModel(FontPrefModel fontPrefModel) {
        this.fontPrefModel = fontPrefModel;
    }

    @Override
    public FontPrefView getFontPrefView() {
        return fontPrefView;
    }

    @Override
    public List<String> getFontStyles() {
        return Arrays.asList(FontPrefModel.PLAIN, FontPrefModel.BOLD, FontPrefModel.ITALIC);
    }

    @Override
    public List<Integer> getFontSizes() {
        return IntStream.range(1, 73).boxed().collect(Collectors.toList());
    }

    @Override
    public void setFontPrefView(FontPrefView fontPrefView) {
        this.fontPrefView = fontPrefView;
    }

    private String createFontStyleString(String fontFamily, String fontStyle, int fontSize) {
        Font font = Font.font(fontFamily, fontSize);
        String styleString = "-fx-font-style: normal";

        if (fontStyle.equals(FontPrefModel.BOLD)) {
            styleString = "-fx-font-weight: bold";
        } else if (fontStyle.equals(FontPrefModel.ITALIC)) {
            styleString = "-fx-font-style: italic";
        }

        return "-fx-font-family: " + fontFamily + ";"
                + styleString + "; -fx-font-size: " + fontSize;
    }

    private String createFontStyleString() {
        return createFontStyleString(fontPrefView.getFontFamily(),
                fontPrefView.getFontStyle(), fontPrefView.getFontSize());
    }

    public boolean validateFontString(ComboBox<String> combo) {
        String fontString = combo.getSelectionModel().getSelectedItem().trim();
        return !fontString.isEmpty() &&
                StreamUtils.exists(combo.getItems().stream(), item -> item.equalsIgnoreCase(fontString));
    }

    public boolean validateFontFamily() {
        boolean valid = validateFontString(fontPrefView.getFontFamilyCombo());
        if (!valid) {
            Errors.headerLessDialog("Font Family Error", "Invalid Font Family");
        }
        return valid;
    }

    public boolean validateFontStyle() {
        boolean valid = validateFontString(fontPrefView.getFontStyleCombo());
        if (!valid) {
            Errors.headerLessDialog("Font Style Error", "Invalid Font Style");
        }
        return valid;
    }

    public boolean validateFontSize() {
        int fontSize = fontPrefView.getFontSize();
        boolean valid = StreamUtils.exists(fontPrefView.getFontSizeCombo().getItems().stream(),
                        item -> item == fontSize);
        if (!valid) {
            Errors.headerLessDialog("Font Size Error", "Invalid Font Size");
        }
        return valid;
    }

    public boolean validateFontValues() {
        return validateFontFamily() && validateFontStyle() && validateFontSize();
    }
}
