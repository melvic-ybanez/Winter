package winter.views.editor;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.function.IntFunction;

/**
 * Created by ybamelcash on 8/7/2015.
 */
public class LineNumberView implements IntFunction<Node> {
    private EditorView editorView;
    
    public LineNumberView(EditorView editorView) {
        this.editorView = editorView;
    }
    
    @Override
    public Node apply(int line) {
        IntFunction<Node> factory = LineNumberFactory.get(editorView);
        Label label = (Label) factory.apply(line);
        label.setBackground(editorView.getBackground());
        return label;
    }
}
