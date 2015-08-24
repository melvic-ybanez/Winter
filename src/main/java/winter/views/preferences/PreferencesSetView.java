package winter.views.preferences;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Created by ybamelcash on 8/24/2015.
 */
public class PreferencesSetView extends Stage {
    private GeneralPrefView generalPrefView;
    private FontPrefView fontPrefView;
    private TabPane tabPane;

    public void init() {
        if (tabPane == null) {
            tabPane = new TabPane();
            Tab generalTab = new Tab("General");
            Tab fontsTab = new Tab("Fonts");

            generalTab.setContent(generalPrefView.getDialogPane());
            fontsTab.setContent(fontPrefView.getDialogPane());

            tabPane.getTabs().addAll(generalTab, fontsTab);

            Scene scene = new Scene(tabPane);
            setScene(scene);
        }
    }

    public GeneralPrefView getGeneralPrefView() {
        return generalPrefView;
    }

    public void setGeneralPrefView(GeneralPrefView generalPrefView) {
        this.generalPrefView = generalPrefView;
    }

    public FontPrefView getFontPrefView() {
        return fontPrefView;
    }

    public void setFontPrefView(FontPrefView fontPrefView) {
        this.fontPrefView = fontPrefView;
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}
