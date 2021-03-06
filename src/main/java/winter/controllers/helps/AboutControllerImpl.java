package winter.controllers.helps;

import javafx.stage.Window;
import winter.models.helps.AboutModel;
import winter.utils.BrowserUtil;
import winter.views.help.AboutView;

/**
 * Created by ybamelcash on 8/27/2015.
 */
public class AboutControllerImpl implements AboutController {
    private AboutModel aboutModel;
    private AboutView aboutView;
    private Window window;

    public AboutControllerImpl(AboutModel aboutModel, Window window) {
        this.aboutModel = aboutModel;
        this.window = window;
    }

    @Override
    public void showUI() {
        if (aboutView == null) {
            aboutView = new AboutView(this, aboutModel, window);
            aboutView.init();
            aboutView.registerEvents();
        }
        aboutView.showAndWait();
    }

    @Override
    public void openProjectWebPage() {
        BrowserUtil.openWebPage(aboutModel.getProjectNameURLString());
    }

    @Override
    public void openAuthorWebPage() {
        BrowserUtil.openWebPage(aboutModel.getAuthorURLString());
    }

    @Override
    public void openIconsSourceWebPage() {
        BrowserUtil.openWebPage(aboutModel.getIconsSourceURLString());
    }

    @Override
    public AboutView getAboutView() {
        return aboutView;
    }

    @Override
    public AboutModel getAboutModel() {
        return aboutModel;
    }
}
