package winter;

import javafx.stage.Stage;

import java.util.Optional;

/**
 * Created by ybamelcash on 6/22/2015.
 */
public class Globals {
    private static Optional<Stage> mainStage = Optional.empty();
    
    public static void setMainStage(Stage stage) {
        mainStage = Optional.of(stage);
    }
    
    public static Stage getMainStage() {
        return mainStage.get();
    }
}
