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
import winter.controllers.editors.EditorSetController;
import winter.controllers.editors.EditorSetControllerImpl;
import winter.controllers.files.FileController;
import winter.controllers.files.FileControllerImpl;
import winter.controllers.projects.ProjectSetController;
import winter.controllers.projects.ProjectSetControllerImpl;
import winter.views.ConsoleView;
import winter.views.ToolBarView;
import winter.views.editor.EditorSetView;
import winter.views.menus.*;
import winter.views.project.ProjectSetView;
import winter.views.repl.REPLView;

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
        
        editorSetController.setFileController(fileController);
        editorSetView.newUntitledTab();
        
        topSplitPane.getItems().addAll(projectSetView, editorSetView);
        topSplitPane.setDividerPositions(0.4f);
        
        bottomSplitPane.getItems().addAll(new ConsoleView(bottomSplitPane.heightProperty()), 
                new REPLView(bottomSplitPane.heightProperty()));
        bottomSplitPane.setDividerPositions(0.5f);
        
        mainSplitPane.getItems().addAll(topSplitPane, bottomSplitPane);
        mainSplitPane.setOrientation(Orientation.VERTICAL);
        mainSplitPane.setDividerPositions(0.8f);

        SplitPane.setResizableWithParent(projectSetView, false);
        
        MenuBar menuBar = new MenuBar();
        FileMenu fileMenu = fileController.getFileMenu();
        EditMenu editMenu = new EditMenu(editorSetController); 
        ToolBarView toolBarView = new ToolBarView(fileMenu, editMenu);
        menuBar.getMenus().addAll(fileMenu, 
                editMenu, new ViewMenu(editorSetController, projectSetController, toolBarView), 
                new PreferencesMenu(), new HelpMenu());
        
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(toolBarView);
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
