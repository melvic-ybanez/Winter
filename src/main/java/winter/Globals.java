package winter;

import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import winter.views.EditorPane;
import winter.views.projects.ProjectsPane;

import java.util.Optional;

/**
 * Created by ybamelcash on 6/22/2015.
 */
public class Globals {
    public static final SplitPane topPane = new SplitPane();
    
    private static Optional<Stage> mainStage = Optional.empty();
    public static final ProjectsPane projectsPane = new ProjectsPane();
    public static final EditorPane editorPane = new EditorPane();
    
    public static void setMainStage(Stage stage) {
        mainStage = Optional.of(stage);
    }
    
    public static Stage getMainStage() {
        return mainStage.get();
    }
}
