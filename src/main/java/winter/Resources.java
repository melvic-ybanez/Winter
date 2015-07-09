package winter;

import javafx.scene.image.ImageView;

/**
 * Created by ybamelcash on 7/9/2015.
 */
public class Resources {
    public static ImageView getIcon(String name) {
        ImageView img = new ImageView(Resources.class.getResource("/icons/" + name).toString());
        int sizeLimit = 16;
        if (img.getImage().getHeight() > sizeLimit) {
            img.setFitHeight(sizeLimit);
            img.setFitWidth(sizeLimit);
        }
        img.setPreserveRatio(true);
        return img;
    }
    
    public static ImageView getRedoIcon() {
        ImageView redoGraphic = Resources.getIcon("undo.png");
        redoGraphic.setScaleX(-1);
        return redoGraphic;
    }
}
