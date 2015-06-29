package winter;

import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import winter.views.ConsolePane;
import winter.views.editors.EditorPane;
import winter.views.projects.ProjectsPane;

import java.util.Optional;

/**
 * Created by ybamelcash on 6/22/2015.
 */
public class Globals {
    public static final SplitPane topPane = new SplitPane();
    public static final SplitPane bottomPane = new SplitPane();
    
    private static Optional<Stage> mainStage = Optional.empty();
    
    public static final ProjectsPane projectsPane = new ProjectsPane();
    public static final EditorPane editorPane = new EditorPane();
    public static final ConsolePane consolePane = new ConsolePane();
    
    public static void setMainStage(Stage stage) {
        mainStage = Optional.of(stage);
    }
    
    public static Stage getMainStage() {
        return mainStage.get();
    }
}
