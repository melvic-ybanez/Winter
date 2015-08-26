package winter.controllers.helps;

import winter.models.helps.AboutModel;
import winter.views.help.AboutView;

/**
 * Created by ybamelcash on 8/27/2015.
 */
public interface AboutController {
    public void showUI();

    public void openProjectWebPage();

    public void openAuthorWebPage();

    public void openIconsSourceWebPage();

    public AboutView getAboutView();

    public AboutModel getAboutModel();
}
