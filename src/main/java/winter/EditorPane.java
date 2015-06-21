package winter;

import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class EditorPane extends BorderPane {
    private CodeArea editorArea = new CodeArea();
    
    public static final String[] KEYWORDS = new String[] {
            "Symbol", "Number", "String", "List", "Nil"
    };
    
    public EditorPane() {
        editorArea.setParagraphGraphicFactory(LineNumberFactory.get(editorArea));
        
        setCenter(editorArea);
    }
}
