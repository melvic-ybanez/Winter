package winter.models.projects;

import javafx.beans.property.BooleanProperty;

import java.nio.file.Path;

/**
 * Created by ybamelcash on 8/7/2015.
 */
public interface ProjectModel {
    public Path getPath();
    
    public String getName();

    public BooleanProperty expandedProperty();

    public boolean isExpanded();
}
