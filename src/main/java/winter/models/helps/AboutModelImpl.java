package winter.models.helps;

import winter.Settings;

/**
 * Created by ybamelcash on 8/27/2015.
 */
public class AboutModelImpl implements AboutModel {
    @Override
    public String getProjectName() {
        return Settings.PROJECT_NAME;
    }

    @Override
    public String getProjectType() {
        return "A Capstone Project presented to the Faculty of the\nCollege of Computer Studies, University of Cebu";
    }

    @Override
    public String getProjectNameURLString() {
        return "https://github.com/melvic-ybanez/Winter";
    }

    @Override
    public String getDescription() {
        return "The text editor for the Meruem programming language";
    }

    @Override
    public String getVersion() {
        return Settings.VERSION;
    }

    @Override
    public String getAuthor() {
        return "Melvic Ybanez";
    }

    @Override
    public String getAuthorURLString() {
        return "https://github.com/melvic-ybanez";
    }

    @Override
    public String getIconsSourceURLString() {
        return "https://icons8.com";
    }
}
