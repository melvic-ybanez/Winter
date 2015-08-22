package winter.controllers.preferences;

import winter.models.preferences.FontPrefModel;
import winter.views.preferences.FontPrefView;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public class FontPrefControllerImpl implements FontPrefController {
    private FontPrefModel fontPrefModel;
    private FontPrefView fontPrefView;

    public FontPrefControllerImpl(FontPrefModel fontPrefModel) {
        setFontPrefModel(fontPrefModel);
        fontPrefModel.setSampleString("; Sample Text. You can edit this one.\n\n" +
                "The quick brown fox jump over the lazy dog");
    }

    @Override
    public List<String> getSystemFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        return Arrays.asList(fontFamilies);
    }

    @Override
    public void applySettings() {

    }

    @Override
    public void showUI() {
        if (fontPrefView == null) {
            setFontPrefView(new FontPrefView(this, fontPrefModel));
            fontPrefView.getFontFamilyCombo().getItems().addAll(getSystemFonts());
            fontPrefView.getFontStyleCombo().getItems().addAll(getFontStyles());
            fontPrefView.getFontSizeCombo().getItems().addAll(getFontSizes());
            fontPrefView.populateWithData();
        }
        fontPrefView.showAndWait();
    }

    @Override
    public void restoreToDefaults() {

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
}
