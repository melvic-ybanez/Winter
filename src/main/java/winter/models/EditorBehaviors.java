package winter.models;

import winter.Settings;
import winter.models.behaviors.*;
import winter.utils.Pair;
import winter.utils.StringUtils;

import java.util.Optional;
import java.util.Stack;
import java.util.function.Function;

/**
 * Created by ybamelcash on 7/17/2015.
 */
public class EditorBehaviors {
    public static ParenIndexesBehavior createDefaultParenIndexes() {
        return editorModel -> paren -> {
            int caretPos = editorModel.getCaretPosition();
            if (caretPos > editorModel.getContents().length()) return Optional.empty();

            Pair<String, String> pair = StringUtils.splitAt(editorModel.getContents(), caretPos);
            String first = pair.getFirst();
            String second = pair.getSecond();

            // check which side the paren is located
            String lastOfFirst = first.isEmpty() ? "" : first.substring(first.length() - 1);
            boolean parenFoundAtLeft = editorModel.withParenHead(lastOfFirst, paren);
            boolean parenFoundAtRight = editorModel.withParenHead(second, paren);

            Optional<Integer> parenToMatchIndexOpt = Optional.empty();
            if (parenFoundAtLeft) {
                parenToMatchIndexOpt = Optional.of(caretPos - 1);       // move backward
            } else if (parenFoundAtRight) {
                parenToMatchIndexOpt = Optional.of(caretPos);
            }

            return parenToMatchIndexOpt.flatMap(parenToMatchIndex -> {
                Optional<Integer> matchIndexOpt = Optional.empty();
                if (paren == '(') {
                    // Remove the first paren from the second string.
                    String second1 = parenFoundAtRight ? second.substring(1) : second;

                    // Add one to delta if the first paren was removed to fill in the missing space.
                    int delta = parenFoundAtRight ? 1 : 0;

                    // Move delta steps forward
                    matchIndexOpt = editorModel.getMatchingParenIndex(second1, paren, ')').map(i -> i + caretPos + delta);
                } else if (paren == ')') {
                    // The traversal here starts at the end since this is to find the opening paren.
                    String reversedFirst = new StringBuilder(first).reverse().toString();

                    // Apply the same logic as above (with a different predicate).
                    String first1 = parenFoundAtLeft ? reversedFirst.substring(1) : reversedFirst;
                    int delta = parenFoundAtLeft ? 1 : 0;

                    // Move delta steps backward, minus one (due to the nature of the caret placement).
                    matchIndexOpt = editorModel.getMatchingParenIndex(first1, paren, '(').map(i -> caretPos - i - 1 - delta);
                }

                return matchIndexOpt.flatMap(matchIndex -> {
                    return Optional.of(Pair.of(parenToMatchIndex, matchIndex));
                });
            });
        };
    }
    
    public static AutoIndentedLineStringBehavior createLispAutoIndentedLineString() {
        return editorModel -> {
            int caretPos = editorModel.getCaretPosition();
            Pair<String, String> pair = StringUtils.splitAt(editorModel.getContents(), caretPos);
            Optional<Integer> openParenIndexOpt = editorModel.getMatchingParenIndex(
                    new StringBuilder(pair.getFirst()).reverse().toString(), ')', '(');

            return openParenIndexOpt.map(openParenIndex -> {
                int realOpenParenIndex = pair.getFirst().length() - openParenIndex - 1;
                String stringBeforeOpenParen = StringUtils.splitAt(pair.getFirst(), realOpenParenIndex).getFirst();
                int startCharCount = 0;

                for (int i = stringBeforeOpenParen.length() - 1; i > -1; i--) {
                    char c = stringBeforeOpenParen.charAt(i);
                    if (c == '\n') break;
                    startCharCount++;
                }

                return "\n" + Settings.TAB_STRING + StringUtils.repeat(startCharCount, " ");
            }).orElseGet(() -> "\n");
        };
    }

    public static MatchingParenIndexBehavior createDefaultMatchingParenIndex() {
        return (str, caretParen, matchingParen) -> {
            Stack<Character> parenStack = new Stack<>();
            Optional<Integer> matchingIndexOpt = Optional.empty();

            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == caretParen) {
                    parenStack.push(c);
                } else if (c == matchingParen) {
                    if (parenStack.isEmpty()) {
                        matchingIndexOpt = Optional.of(i);
                        break;
                    } else {
                        parenStack.pop();
                    }
                }
            }

            return matchingIndexOpt;
        };
    }

    public static StyleClassBehavior createMeruemStyleClass() {
        return (matcher, parenIndex1, parenIndex2) -> {
            String styleClass = null;
            if (matcher.group("TYPE") != null) styleClass = "type";
            if (matcher.group("OPERATOR") != null) styleClass = "operator";
            if (matcher.group("FUNCTION") != null) styleClass = "function-name";
            if (matcher.group("DEFINE") != null) styleClass = "define-command";
            if (matcher.group("SPECIAL") != null) styleClass = "special-keyword";
            if (matcher.group("PAREN") != null) {
                if (matcher.start() == parenIndex1 || matcher.start() == parenIndex2) {
                    styleClass = "focused-paren";
                }
            }
            if (matcher.group("BRACE") != null) styleClass = "brace";
            if (matcher.group("STRING") != null) styleClass = "string";
            if (matcher.group("CHAR") != null) styleClass = "char";
            if (matcher.group("COMMENT") != null) styleClass = "comment";
            if (matcher.group("QUOTE") != null) styleClass = "quote";
            return styleClass;
        };
    }

    public static ActiveParenIndexesBehavior createDefaultActiveParenIndexes() {
        return editorModel -> {
            Function<Character, Optional<Pair<Integer, Integer>>> getIndexes = 
                    createDefaultParenIndexes().apply(editorModel);
            Optional<Pair<Integer, Integer>> parenIndexes = getIndexes.apply('(');
            if (parenIndexes.isPresent()) return parenIndexes;
            else return getIndexes.apply(')');
        };
    }
}
