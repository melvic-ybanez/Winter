package winter;

import javafx.scene.image.ImageView;

/**
 * Created by ybamelcash on 7/9/2015.
 */
public class Resources {
    public static ImageView getIcon(String name, String extension) {
        ImageView img = new ImageView(Resources.class.getResource("/icons/" + name + "." + extension).toString());
        int sizeLimit = 20;
        if (img.getImage().getHeight() > sizeLimit) {
            img.setFitHeight(sizeLimit);
            img.setFitWidth(sizeLimit);
        }
        img.setPreserveRatio(true);
        return img;
    }

    public static ImageView getIcon(String name) {
        return getIcon(name, "png");
    }
}
