package winter.views.edit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import winter.Resources;

/**
 * Created by ybamelcash on 7/8/2015.
 */
public class FindPane extends HBox {
    private TextField findField = new TextField();
    
    public FindPane() {
        setId("find-pane");
        getStyleClass().add("meruem-toolbar");
        findField.setPromptText("Enter the string to search");
        
        HBox checkBoxPane = new HBox();
        CheckBox matchCaseCheck = new CheckBox("Match Case");
        CheckBox wordsCheck = new CheckBox("Words");
        checkBoxPane.getChildren().addAll(matchCaseCheck, wordsCheck);
        checkBoxPane.setAlignment(Pos.CENTER);
        checkBoxPane.setSpacing(7);
        
        HBox buttonsPane = new HBox();
        Button nextButton = new Button();
        Button previousButton = new Button();
        nextButton.setTooltip(new Tooltip("Find next occurrence"));
        previousButton.setTooltip(new Tooltip("Find previous occurrence"));
        nextButton.setGraphic(Resources.getIcon("up.png"));
        previousButton.setGraphic(Resources.getIcon("down.png"));
        buttonsPane.getChildren().addAll(nextButton, previousButton);
        buttonsPane.setAlignment(Pos.CENTER);
        buttonsPane.setSpacing(7);
        
        getChildren().addAll(findField, checkBoxPane, buttonsPane);
        setStyle("-fx-spacing: 10");
        setPadding(new Insets(3, 3, 3, 3));
    }
}
