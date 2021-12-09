package by.victory.client.validation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public record TextFieldListener(TextField textField, Pattern pattern) implements ChangeListener<String> {
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches(pattern.inTimeTemplateToString())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Сообщение");
            alert.setContentText("Данные не соответствуют требуемому формату. "+pattern.clarificationToString());
            alert.showAndWait();

            textField.setText(oldValue);
        }
    }
}

