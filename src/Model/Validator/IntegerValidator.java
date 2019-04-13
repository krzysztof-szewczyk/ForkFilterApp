package Model.Validator;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class IntegerValidator implements UnaryOperator<TextFormatter.Change> {

    private String regexp;

    public IntegerValidator(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        String newText = change.getControlNewText();
        if (newText.matches(regexp)) {
            return change;
        }
        return null;
    }
}
