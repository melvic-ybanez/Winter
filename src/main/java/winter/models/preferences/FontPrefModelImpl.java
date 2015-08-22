package winter.models.preferences;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public class FontPrefModelImpl implements FontPrefModel {
    private String fontFamily;
    private int fontSize;
    private String fontStyle;

    @Override
    public String getFontFamily() {
        return fontFamily;
    }

    @Override
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    @Override
    public int getFontSize() {
        return fontSize;
    }

    @Override
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public String getFontStyle() {
        return fontStyle;
    }

    @Override
    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    @Override
    public void reset() {

    }
}
