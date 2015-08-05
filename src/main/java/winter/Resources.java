package winter;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * Created by ybamelcash on 7/9/2015.
 */
public class Resources {
    public static ImageView getIcon(String name) {
        ImageView img = new ImageView(Resources.class.getResource("/icons/" + name).toString());
        int sizeLimit = 24;
        if (img.getImage().getHeight() > sizeLimit) {
            img.setFitHeight(sizeLimit);
            img.setFitWidth(sizeLimit);
        }
        img.setPreserveRatio(true);
        return img;
    }
}
