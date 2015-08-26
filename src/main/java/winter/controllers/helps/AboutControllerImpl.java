package winter.controllers.helps;

import javafx.stage.Window;
import winter.views.help.AboutView;

/**
 * Created by ybamelcash on 8/27/2015.
 */
public class AboutControllerImpl implements AboutController {
    private AboutView aboutView;
    private Window window;

    public AboutControllerImpl(Window window) {
        this.window = window;
    }

    @Override
    public void showUI() {
        if (aboutView == null) {
            aboutView = new AboutView(window);
            aboutView.init();
        }
        aboutView.showAndWait();
    }

    @Override
    public void openProjectWebPage() {

    }

    @Override
    public void openAuthorWebPage() {

    }

    @Override
    public void openIconsSourceWebPage() {

    }

    @Override
    public AboutView getAboutView() {
        return null;
    }
}
