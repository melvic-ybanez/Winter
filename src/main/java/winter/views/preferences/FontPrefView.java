package winter.views.preferences;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import winter.views.editor.EditorView;

/**
 * Created by ybamelcash on 8/22/2015.
 */
public class FontPrefView extends Dialog<ButtonType> {
    private ComboBox<String> fontFamilyCombo;
    private ComboBox<String> styleCombo;
    private ComboBox<Integer> sizeCombo;
    private CodeArea sampleEditor;
    private Button resetButton;

    public FontPrefView() {
        super();
        setTitle("Font Settings");
        init();
        registerEvents();
    }

    private void init() {
        fontFamilyCombo = new ComboBox<>();
        styleCombo = new ComboBox<>();
        sizeCombo = new ComboBox<>();
        sampleEditor = new CodeArea();
        resetButton = new Button("Reset to Defaults");

        sampleEditor.getStyleClass().add("sample-code-area");
        int space = 10;

        HBox fontFamilyPane = new HBox();
        fontFamilyPane.getChildren().addAll(new Label("Font Family:"), fontFamilyCombo);
        fontFamilyPane.setSpacing(space);

        HBox fontStylePane = new HBox();
        fontStylePane.getChildren().addAll(new Label("Font Style:"), styleCombo);
        fontStylePane.setSpacing(space);

        HBox fontSizePane = new HBox();
        fontSizePane.getChildren().addAll(new Label("Font Size:"), sizeCombo);
        fontSizePane.setSpacing(space);

        HBox fontPane = new HBox();
        fontPane.getChildren().addAll(fontFamilyPane, fontStylePane, fontSizePane);
        fontPane.setSpacing(space * 3);

        BorderPane resetPane = new BorderPane();
        resetPane.setRight(resetButton);

        BorderPane centerPane = new BorderPane();
        centerPane.setCenter(sampleEditor);
        centerPane.setPadding(new Insets(20, 0, 20, 0));

        BorderPane mainPane = new BorderPane();
        mainPane.setTop(fontPane);
        mainPane.setCenter(centerPane);
        mainPane.setBottom(resetPane);

        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
        getDialogPane().setContent(mainPane);

        getDialogPane().getScene().getStylesheets().add(FontPrefView.class.getResource("/styles/preferences.css").toExternalForm());
    }

    private void registerEvents() {

    }
}
