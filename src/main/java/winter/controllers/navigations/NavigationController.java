package winter.controllers.navigations;

import javafx.stage.Stage;
import winter.models.editors.EditorModel;
import winter.views.navigation.NavigationView;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Created by ybamelcash on 8/18/2015.
 */
public interface NavigationController {
    public void goToLine();

    public void showGoToFileUI();

    public NavigationView getNavigationView();

    public void populateFilesView(List<EditorModel> editorModels);

    public void filenameAutoCompleteOnType();

    public void selectFilename();

    public void selectFilename(EditorModel editorModel);
}
