package winter.controllers.preferences;

import javafx.scene.control.ButtonType;
import winter.models.preferences.GeneralPrefModel;
import winter.utils.Errors;
import winter.utils.StreamUtils;
import winter.utils.StringUtils;
import winter.views.preferences.GeneralPrefView;

import java.util.Optional;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public class GeneralPrefControllerImpl implements GeneralPrefController {
    private GeneralPrefModel generalPrefModel;
    private GeneralPrefView generalPrefView;

    public GeneralPrefControllerImpl(GeneralPrefModel generalPrefModel) {
        setGeneralPrefModel(generalPrefModel);
        setGeneralPrefView(new GeneralPrefView(this, generalPrefModel));
        generalPrefView.initData();
    }

    @Override
    public void showUI() {
        Optional<ButtonType> result = generalPrefView.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.APPLY) {
                applySettings();
            } else if (buttonType == ButtonType.CANCEL) {
                generalPrefModel.reset();
            }
        });
    }

    @Override
    public void applySettings() {
        String tabSpaceCountString = generalPrefView.getSpaceCountField().getText().trim();
        Optional<Integer> tabSpaceCountOpt = StringUtils.asInteger(tabSpaceCountString);
        if (!tabSpaceCountOpt.isPresent()) {
            Errors.headerLessDialog("Number of Spaces Error", "Invalid number of spaces");
        }
        tabSpaceCountOpt.ifPresent(generalPrefModel::setTabSpaceCount);
        generalPrefModel.setSaveFilesBeforeExit(generalPrefView.getSaveFilesBeforeExitBox().isSelected());
    }

    @Override
    public void resetToDefaults() {
        generalPrefModel.reset();
    }

    @Override
    public void setGeneralPrefView(GeneralPrefView generalPrefView) {
        this.generalPrefView = generalPrefView;
    }

    @Override
    public GeneralPrefView getGeneralPrefView() {
        return generalPrefView;
    }

    @Override
    public GeneralPrefModel getGeneralPrefModel() {
        return generalPrefModel;
    }

    public void setGeneralPrefModel(GeneralPrefModel generalPrefModel) {
        this.generalPrefModel = generalPrefModel;
    }
}
