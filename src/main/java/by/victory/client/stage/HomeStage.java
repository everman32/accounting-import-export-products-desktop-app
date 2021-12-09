package by.victory.client.stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeStage {
    public void main(Stage primaryStage, String view) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeStage.class.getResource(View.PACKAGE_TO_VIEW +
                view + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Главное окно");
        stage.setScene(scene);

        stage.setOnHidden(e -> primaryStage.show());
        primaryStage.hide();
        stage.show();
    }
}
