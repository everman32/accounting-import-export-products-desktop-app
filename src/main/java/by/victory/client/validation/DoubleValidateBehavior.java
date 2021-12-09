package by.victory.client.validation;

import by.victory.client.behavior.AlertBehavior;
import javafx.scene.control.TextField;

public interface DoubleValidateBehavior extends AlertBehavior {
    default boolean isDoubleValid(TextField textField){
        try {
            if (Double.parseDouble(textField.getText()) <= 0.0) {
                showAlert(Pattern.DOUBLE.clarificationToString());
                textField.setText("");
                return false;
            }
        } catch (Throwable e){
            e.printStackTrace();
            showAlert(Pattern.DOUBLE.clarificationToString());
            return false;
        }
        return true;
    }
}
