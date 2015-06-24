package winter;

import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import winter.views.ProjectsPane;

import java.util.Optional;

/**
 * Created by ybamelcash on 6/22/2015.
 */
public class Globals {
    public static final SplitPane topPane = new SplitPane();
    
    private static Optional<Stage> mainStage = Optional.empty();
    private static final ProjectsPane projectsPane = new ProjectsPane();
    
    public static void setMainStage(Stage stage) {
        mainStage = Optional.of(stage);
    }
    
    public static Stage getMainStage() {
        return mainStage.get();
    }
    
    public static ProjectsPane getProjectsPane() {
        return projectsPane;
    }
}
