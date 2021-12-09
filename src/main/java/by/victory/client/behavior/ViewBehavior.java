package by.victory.client.behavior;

import by.victory.client.stage.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public interface ViewBehavior extends AlertBehavior{
    default Parent loadView(String view) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(View.PACKAGE_TO_VIEW +
                    view + ".fxml")));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(e.getMessage());
        }
        return root;
    }

    void setViewCenter(String view);
}
