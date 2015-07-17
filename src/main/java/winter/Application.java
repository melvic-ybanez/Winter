package winter;

import javafx.application.Platform;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import winter.controllers.EditorsControllerImpl;
import winter.views.ConsolePane;
import winter.views.editors.EditorsView;
import winter.views.menus.FileMenu;
import winter.views.projects.ProjectsView;
import winter.views.repl.REPLPane;

import java.util.Optional;

/**
 * Created by ybamelcash on 6/22/2015.
 */
public class Application {
    public static final SplitPane mainSplitPane = new SplitPane();
    public static final SplitPane topSplitPane = new SplitPane();
    public static final SplitPane bottomSplitPane = new SplitPane();
    
    private static Optional<Stage> mainStage = Optional.empty();
    
    public static final ProjectsView PROJECTS_VIEW = new ProjectsView();
    public static final ConsolePane consolePane = new ConsolePane();
    public static final REPLPane replPane = new REPLPane();
    
    public static class menus {
        public static final FileMenu fileMenu = new FileMenu();
    }
    
    public static void setMainStage(Stage stage) {
        mainStage = Optional.of(stage);
    }
    
    public static Stage getMainStage() {
        return mainStage.get();
    }
    
    public static void exit() {
        if (EditorsControllerImpl.closeAllTabs()) {
            Platform.exit();
        }
    }
}
