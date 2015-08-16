package winter.factories;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import winter.Resources;

/**
 * Created by ybamelcash on 7/20/2015.
 */
public class Icons {
    public static ImageView createNewFileIcon() {
        return Resources.getIcon("new");
    }

    public static ImageView createOpenFileIcon() {
        return Resources.getIcon("open_file");
    }

    public static ImageView createClosedDirectoryIcon() {
        return Resources.getIcon("close_folder");
    }

    public static ImageView createSaveIcon() {
        return Resources.getIcon("save");
    }

    public static ImageView createSaveAsIcon() {
        return Resources.getIcon("save_as");
    }

    public static ImageView createUndoIcon() {
        return Resources.getIcon("undo");
    }

    public static ImageView createRedoIcon() {
        return Resources.getIcon("redo");
    }

    public static ImageView createFindIcon() {
        return Resources.getIcon("find");
    }

    public static ImageView createReplaceIcon() {
        return Resources.getIcon("replace");
    }

    public static ImageView createCopyIcon() {
        return Resources.getIcon("copy");
    }

    public static ImageView createCutIcon() {
        return Resources.getIcon("cut");
    }

    public static ImageView createPasteIcon() {
        return Resources.getIcon("paste");
    }

    public static ImageView createRestartIcon() {
        return Resources.getIcon("restart");
    }

    public static ImageView createFindFileIcon() {
        return Resources.getIcon("find_file");
    }

    public static ImageView createProjectsIcon() {
        return Resources.getIcon("view_projects");
    }

    public static ImageView createPreferencesIcon() {
        return Resources.getIcon("preferences");
    }

    public static ImageView createHelpIcon() {
        return Resources.getIcon("help");
    }

    public static ImageView createDeleteIcon() {
        return Resources.getIcon("delete");
    }

    public static ImageView createRenameIcon() {
        return Resources.getIcon("rename");
    }

    public static ImageView createMoveIcon() {
        return Resources.getIcon("move");
    }

    public static ImageView createRefreshIcon() {
        return Resources.getIcon("refresh");
    }

    public static ImageView createCloseIcon() {
        return Resources.getIcon("close");
    }

    public static ImageView createFileIcon() {
        return Resources.getIcon("file");
    }

    public static ImageView createOpenDirectoryIcon() {
        return Resources.getIcon("open_folder");
    }

    public static ImageView createDownIcon() {
        return Resources.getIcon("down");
    }

    public static ImageView createUpIcon() {
        return Resources.getIcon("up");
    }

    public static ImageView createHideIcon() {
        return Resources.getIcon("hide");
    }

    public static Button createIconedButton(ImageView iconView, String tooltip) {
        Button icon = new Button("", iconView);
        icon.setTooltip(new Tooltip(tooltip));
        return icon;
    }

    public static Button createIconedButton(ImageView iconView) {
        return createIconedButton(iconView, "");
    }

    public static Button createIconedButton(String iconName, String tooltip) {
        return createIconedButton(Resources.getIcon(iconName), tooltip);
    }
    
    public static Button createIconedButton(String iconName) {
        return createIconedButton(iconName, "");
    }
}
