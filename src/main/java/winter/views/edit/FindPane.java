package winter.views.edit;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;

/**
 * Created by ybamelcash on 7/8/2015.
 */
public class FindPane extends HBox {
    private TextField findField = new TextField();
    
    public FindPane() {
        findField.setPromptText("Enter the string to search");
        CheckBox matchCaseCheck = new CheckBox("Match Case");
        CheckBox wordsCheck = new CheckBox("Words");
        
        Button nextButton = new Button("Next");
        Button previousButton = new Button("Prev");
        getChildren().addAll(findField, matchCaseCheck, wordsCheck, nextButton, previousButton);
    }
}
