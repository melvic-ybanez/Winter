package winter.controllers.preferences;

import javafx.application.Platform;
import winter.models.preferences.GeneralPrefModel;
import winter.models.preferences.PreferencesModel;
import winter.models.preferences.PreferencesView;
import winter.utils.Errors;
import winter.utils.StringUtils;
import winter.views.preferences.GeneralPrefView;

import java.util.Optional;

/**
 * Created by ybamelcash on 8/20/2015.
 */
public class GeneralPrefControllerImpl extends BasePrefController implements GeneralPrefController {
    private GeneralPrefModel generalPrefModel;
    private GeneralPrefView generalPrefView;
    private FontPrefController fontPrefController;

    public GeneralPrefControllerImpl(GeneralPrefModel generalPrefModel) {
        setGeneralPrefModel(generalPrefModel);
        setFontPrefController(fontPrefController);
    }

    @Override
    public void applySettings() {
        String tabSpaceCountString = generalPrefView.getSpaceCountField().getText().trim();
        Optional<Integer> tabSpaceCountOpt = StringUtils.asInteger(tabSpaceCountString);
        if (!tabSpaceCountOpt.isPresent()) {
            Errors.headerLessDialog("Number of Spaces Error", "Invalid number of spaces");
            generalPrefView.populateWithData();
            showUI();
            return;
        }
        tabSpaceCountOpt.ifPresent(generalPrefModel::setTabSpaceCount);
        generalPrefModel.setSaveFilesBeforeExit(generalPrefView.getSaveFilesBeforeExitBox().isSelected());
        generalPrefModel.setRemoveExtraSpaces(generalPrefView.getRemoveExtraSpacesBox().isSelected());
        generalPrefModel.setWrapText(generalPrefView.getWrapTextBox().isSelected());
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

    @Override
    public void changeFontSettings() {
        generalPrefView.close();
        Platform.runLater(() -> getFontPrefController().showUI());
    }

    @Override
    public PreferencesView getView() {
        return getGeneralPrefView();
    }

    @Override
    public PreferencesModel getModel() {
        return getGeneralPrefModel();
    }

    @Override
    public void initPreferencesView() {
        setGeneralPrefView(new GeneralPrefView(this, generalPrefModel));
        generalPrefView.populateWithData();
    }

    @Override
    public void setGeneralPrefModel(GeneralPrefModel generalPrefModel) {
        this.generalPrefModel = generalPrefModel;
    }

    @Override
    public FontPrefController getFontPrefController() {
        return fontPrefController;
    }

    public void setFontPrefController(FontPrefController fontPrefController) {
        this.fontPrefController = fontPrefController;
    }
}
