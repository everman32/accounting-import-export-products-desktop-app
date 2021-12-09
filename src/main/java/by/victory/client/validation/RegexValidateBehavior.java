package by.victory.client.validation;

import by.victory.client.behavior.AlertBehavior;
import javafx.scene.control.TextField;

public interface RegexValidateBehavior extends AlertBehavior {
    default boolean isValid(TextField textField, Pattern pattern){
        if (!textField.getText().matches(pattern.finalTemplateToString())) {
            showAlert("Данные не соответствуют требуемому формату. "+pattern.clarificationToString());
            textField.setText("");
            return false;
        } else return true;
    }
}
