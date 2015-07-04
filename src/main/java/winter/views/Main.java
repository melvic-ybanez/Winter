package winter.views;

/**
 * Created by ybamelcash on 6/21/2015.
 */
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import winter.Globals;
import winter.views.menus.EditMenu;
import winter.views.menus.FileMenu;
import winter.views.menus.HelpMenu;
import winter.views.menus.PreferencesMenu;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SplitPane mainPane = Globals.mainPane;
        SplitPane topPane = Globals.topPane;
        SplitPane bottomPane = Globals.bottomPane;
        
        topPane.getItems().addAll(Globals.projectsPane, Globals.editorPane);
        topPane.setDividerPositions(0.4f);
        
        bottomPane.getItems().addAll(Globals.consolePane, Globals.replPane);
        bottomPane.setDividerPositions(0.5f);
        
        mainPane.getItems().addAll(topPane, bottomPane);
        mainPane.setOrientation(Orientation.VERTICAL);
        mainPane.setDividerPositions(0.8f);
        
        SplitPane.setResizableWithParent(Globals.projectsPane, false);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(new FileMenu(), new EditMenu(), new PreferencesMenu(), new HelpMenu());

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainPane);
        
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
