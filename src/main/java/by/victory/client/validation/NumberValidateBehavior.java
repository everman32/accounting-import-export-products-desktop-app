package by.victory.client.validation;

import by.victory.client.behavior.AlertBehavior;
import javafx.scene.control.TextField;


public interface NumberValidateBehavior extends AlertBehavior {
    default boolean isNumberValid(TextField textField){
        try {
            if (Integer.parseInt(textField.getText()) < 1) {
                showAlert(Pattern.NUMBER.clarificationToString());
                textField.setText("");
                return false;
            }
        } catch (Throwable e){
            e.printStackTrace();
            showAlert(Pattern.NUMBER.clarificationToString());
        }
        return true;
    }
}