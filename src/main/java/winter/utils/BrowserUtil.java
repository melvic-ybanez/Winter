package winter.utils;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by ybamelcash on 8/27/2015.
 */
public class BrowserUtil {
    public static void openWebPage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebPage(URL url) {
        try {
            openWebPage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void openWebPage(String urlString) {
        try {
            openWebPage(new URL(urlString));
        } catch (MalformedURLException e) {
            Errors.exceptionDialog("URL Exception", "A malformed url exception occured",
                    e.getMessage(), e);
        }
    }
}
