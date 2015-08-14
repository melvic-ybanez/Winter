package winter.views;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/**
 * Created by ybamelcash on 8/15/2015.
 */
public class RequiredTextInputDialog extends TextInputDialog {
    public RequiredTextInputDialog(String defaultAnswer) {
        super(defaultAnswer);
    }

    public Optional<String> getAnswer() {
        Optional<String> result = super.showAndWait();
        return result.map(answer -> {
            String cleanAnswer = answer.trim();
            if (cleanAnswer.isEmpty()) {
                return getDefaultValue();
            }
            return cleanAnswer;
        });
    }
}
