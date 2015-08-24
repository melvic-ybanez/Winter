package winter.models.preferences;

import org.apache.commons.lang.SystemUtils;

import java.awt.*;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public class FontPrefModelImpl implements FontPrefModel {
    private String fontFamily;
    private int fontSize;
    private String fontStyle;
    private String sampleString;

    public FontPrefModelImpl() {
        reset();
    }

    @Override
    public String getFontFamily() {
        return fontFamily;
    }

    @Override
    public String getDefaultFontFamily() {
        return Font.MONOSPACED;
    }

    @Override
    public String getDefaultFontStyle() {
        return fontStyleToString(Font.PLAIN);
    }

    @Override
    public int getDefaultFontSize() {
        return 12;
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
    public void setSampleString(String sampleString) {
        this.sampleString = sampleString;
    }

    @Override
    public String getSampleString() {
        return sampleString;
    }

    @Override
    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    @Override
    public void reset() {
        setFontFamily(getDefaultFontFamily());
        setFontStyle(getDefaultFontStyle());
        setFontSize(getDefaultFontSize());
    }

    private String fontStyleToString(int intFontStyle) {
        switch (intFontStyle) {
            case Font.PLAIN: return PLAIN;
            case Font.BOLD: return BOLD;
            case Font.ITALIC: return ITALIC;
        }
        return "";
    }

    private int fontStyleToInt(String stringFontStyle) {
        switch (stringFontStyle) {
            case PLAIN: return Font.PLAIN;
            case BOLD: return Font.BOLD;
            case ITALIC: return Font.ITALIC;
        }
        return -1;
    }
}
