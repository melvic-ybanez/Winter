package winter;

/**
 * Created by ybamelcash on 6/21/2015.
 */
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import winter.menus.EditMenu;
import winter.menus.FileMenu;
import winter.menus.HelpMenu;
import winter.menus.PreferencesMenu;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane projectPane = new ProjectPane();
        Pane consolePane = new ConsolePane();
        EditorPane editorPane = new EditorPane();
        
        SplitPane mainSplitPane = new SplitPane();
        
        SplitPane topPane = new SplitPane();
        topPane.getItems().addAll(projectPane, editorPane);
        topPane.setDividerPositions(0.4f);
        
        mainSplitPane.getItems().addAll(topPane, consolePane);
        mainSplitPane.setOrientation(Orientation.VERTICAL);
        mainSplitPane.setDividerPositions(0.6f);
        
        SplitPane.setResizableWithParent(projectPane, false);
        SplitPane.setResizableWithParent(consolePane, false);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(new FileMenu(editorPane), new EditMenu(), new PreferencesMenu(), new HelpMenu());

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainSplitPane);
        
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(Main.class.getResource("/syntax/meruem.css").toExternalForm());
        
        Globals.setMainStage(primaryStage);
        
        primaryStage.setTitle("Winter");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
