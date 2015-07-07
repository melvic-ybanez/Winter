package winter.views;

/**
 * Created by ybamelcash on 6/21/2015.
 */
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import winter.Application;
import winter.views.menus.EditMenu;
import winter.views.menus.HelpMenu;
import winter.views.menus.PreferencesMenu;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SplitPane mainSplitPane = Application.mainSplitPane;
        SplitPane topPane = Application.topSplitPane;
        SplitPane bottomPane = Application.bottomSplitPane;
        
        topPane.getItems().addAll(Application.projectsPane, Application.editorPane);
        topPane.setDividerPositions(0.4f);
        
        bottomPane.getItems().addAll(Application.consolePane, Application.replPane);
        bottomPane.setDividerPositions(0.5f);
        
        mainSplitPane.getItems().addAll(topPane, bottomPane);
        mainSplitPane.setOrientation(Orientation.VERTICAL);
        mainSplitPane.setDividerPositions(0.8f);

        SplitPane.setResizableWithParent(Application.projectsPane, false);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(Application.menus.fileMenu, new EditMenu(), new PreferencesMenu(), new HelpMenu());

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainSplitPane);
        
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(Main.class.getResource("/syntax/meruem.css").toExternalForm());
        
        Application.setMainStage(primaryStage);
        
        primaryStage.setTitle("Winter");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
