package winter.controllers;

import winter.models.ProjectModel;
import winter.utils.Either;
import winter.Globals;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public class ProjectController {
    public static Either<String, Path> addFile(Path projectPath, Path fileToAddPath) {
        if (!Files.exists(projectPath)) {
            return Either.left("Project does not exist: " + projectPath.toString());
        } else {
            ProjectModel projectModel = new ProjectModel(projectPath);
            return projectModel.addFile(fileToAddPath);
        } 
    }
}
