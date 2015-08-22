package winter.models.preferences;

import java.util.List;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public interface FontPrefModel {
    public void setFontFamily(String fontFamily);

    public String getFontFamily();

    public void setFontSize(int fontSize);

    public int getFontSize();

    public void setFontStyle(String fontStyle);

    public String getFontStyle();

    public void reset();
}
