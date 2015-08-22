package winter.controllers.preferences;

import winter.models.preferences.FontPrefModel;
import winter.views.preferences.FontPrefView;

import java.util.List;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public interface FontPrefController {
    public List<String> getSystemFonts();

    public void applySettings();

    public void showUI();

    public void resetToDefaults();

    public void setFontPrefModel(FontPrefModel fontPrefModel);

    public FontPrefModel getFontPrefModel();

    public void setFontPrefView(FontPrefView fontPrefView);

    public FontPrefView getFontPrefView();

    public List<String> getFontStyles();

    public List<Integer> getFontSizes();
}
