package by.victory.client.behavior;

import javafx.scene.control.Alert;

public interface AlertBehavior {
    default void showAlert(String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Сообщение");

        alert.setContentText(content);
        alert.showAndWait();
    }
}
