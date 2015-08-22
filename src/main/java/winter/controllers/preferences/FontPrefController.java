package winter.controllers.preferences;

import winter.models.preferences.FontPrefModel;
import winter.views.preferences.FontPrefView;

import java.util.List;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public interface FontPrefController extends PreferencesController {
    public List<String> getSystemFonts();

    public void setFontPrefModel(FontPrefModel fontPrefModel);

    public void setFontPrefView(FontPrefView fontPrefView);

    public FontPrefView getFontPrefView();

    public FontPrefModel getFontPrefModel();

    public List<String> getFontStyles();

    public List<Integer> getFontSizes();
}
