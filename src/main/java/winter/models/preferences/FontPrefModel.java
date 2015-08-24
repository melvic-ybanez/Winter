package winter.models.preferences;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public interface FontPrefModel extends PreferencesModel {
    public static final String PLAIN = "Plain";
    public static final String BOLD = "Bold";
    public static final String ITALIC = "Italic";


    public String getDefaultFontFamily();

    public String getDefaultFontStyle();

    public int getDefaultFontSize();

    public void setFontFamily(String fontFamily);

    public String getFontFamily();

    public void setFontSize(int fontSize);

    public int getFontSize();

    public void setFontStyle(String fontStyle);

    public String getFontStyle();

    public void setSampleString(String sampleString);

    public String getSampleString();
}
