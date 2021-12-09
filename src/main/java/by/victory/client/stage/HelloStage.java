package by.victory.client.stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloStage extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloStage.class.getResource(View.PACKAGE_TO_VIEW +
                View.AUTHORIZATION.toString() + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Начальное окно");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[]args){
        launch(args);
    }
}