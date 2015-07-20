package winter.factories;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import winter.Resources;

/**
 * Created by ybamelcash on 7/20/2015.
 */
public class Icons {
    public static ImageView getRedoImageView() {
        ImageView redoGraphic = Resources.getIcon("undo.png");
        redoGraphic.setScaleX(-1);
        return redoGraphic;
    }
    
    public static Button getRedoButton() {
        Button icon = new Button("", getRedoImageView());
        icon.setTooltip(new Tooltip("Redo"));
        return icon;
    }

    public static Button createButtonIcon(String iconName, String tooltip) {
        Button icon = new Button("", Resources.getIcon(iconName));
        icon.setTooltip(new Tooltip(tooltip));
        return icon;
    }
    
    public static Button createButtonIcon(String iconName) {
        return createButtonIcon(iconName, "");
    }
}
