package winter.controllers.preferences;

import winter.views.preferences.FontPrefView;

import java.util.List;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public class FontPrefControllerImpl implements FontPrefController {
    private FontPrefView fontPrefView;

    public FontPrefControllerImpl() {

    }

    @Override
    public List<String> getSystemFonts() {
        return null;
    }

    @Override
    public void applySettings() {

    }

    @Override
    public void showUI() {
        if (fontPrefView == null) {
            setFontPrefView(new FontPrefView());
        }
        fontPrefView.showAndWait();
    }

    @Override
    public void restoreToDefaults() {

    }

    @Override
    public FontPrefView getFontPrefView() {
        return fontPrefView;
    }

    @Override
    public void setFontPrefView(FontPrefView fontPrefView) {
        this.fontPrefView = fontPrefView;
    }
}
