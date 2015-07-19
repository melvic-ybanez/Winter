package winter;

/**
 * Created by ybamelcash on 6/21/2015.
 */

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import winter.controllers.*;
import winter.views.ConsolePane;
import winter.views.ToolBarPane;
import winter.views.editors.EditorSetView;
import winter.views.menus.EditMenu;
import winter.views.menus.FileMenu;
import winter.views.menus.HelpMenu;
import winter.views.menus.PreferencesMenu;
import winter.views.projects.ProjectSetView;
import winter.views.repl.REPLPane;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SplitPane mainSplitPane = new SplitPane();
        SplitPane topSplitPane = new SplitPane();
        SplitPane bottomSplitPane = new SplitPane();

        EditorSetController editorSetController = new EditorSetControllerImpl();
        EditorSetView editorSetView = editorSetController.getEditorSetView();
        ProjectSetController projectSetController = new ProjectSetControllerImpl(editorSetView, topSplitPane.heightProperty());
        ProjectSetView projectSetView = projectSetController.getProjectSetView();
        FileController fileController = new FileControllerImpl(editorSetController, projectSetController);
        FileMenu fileMenu = fileController.getFileMenu();
        
        editorSetController.setFileController(fileController);
        
        topSplitPane.getItems().addAll(projectSetView, editorSetView);
        topSplitPane.setDividerPositions(0.4f);
        
        bottomSplitPane.getItems().addAll(new ConsolePane(bottomSplitPane.heightProperty()), 
                new REPLPane(bottomSplitPane.heightProperty()));
        bottomSplitPane.setDividerPositions(0.5f);
        
        mainSplitPane.getItems().addAll(topSplitPane, bottomSplitPane);
        mainSplitPane.setOrientation(Orientation.VERTICAL);
        mainSplitPane.setDividerPositions(0.8f);

        SplitPane.setResizableWithParent(projectSetView, false);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, 
                new EditMenu(editorSetController), new PreferencesMenu(), new HelpMenu());
        
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(new ToolBarPane());
        mainPane.setCenter(mainSplitPane);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainPane);
        
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(Main.class.getResource("/syntax/meruem.css").toExternalForm());
        
        primaryStage.setTitle("Winter");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(event -> {
            if (!editorSetController.closeAllTabs()) {
                event.consume();
            }
        });
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
