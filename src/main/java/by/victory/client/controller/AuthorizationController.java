package by.victory.client.controller;

import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.behavior.ViewBehavior;
import by.victory.client.model.Authorized;
import by.victory.client.model.Connection;
import by.victory.client.stage.HomeStage;
import by.victory.client.stage.View;
import by.victory.client.validation.Pattern;
import by.victory.client.validation.RegexValidateBehavior;
import by.victory.client.validation.TextFieldListener;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.json.JsonObject;
import java.io.IOException;


public class AuthorizationController implements ViewBehavior, SocketBehavior, ResponseBehavior,
        RegexValidateBehavior {
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField loginText;
    @FXML
    public TextField portText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Button buttonLogin;
    @FXML
    private Hyperlink buttonRegister;

    @FXML
    void initialize() {
        loginText.textProperty().addListener(new TextFieldListener(loginText,
                Pattern.AUTHORIZATION));
        passwordText.textProperty().addListener(new TextFieldListener(passwordText,
                Pattern.AUTHORIZATION));
        portText.textProperty().addListener(new TextFieldListener(portText,
                Pattern.NUMBER));

        buttonLogin.disableProperty().bind(Bindings.isEmpty(loginText.textProperty())
                .or(Bindings.isEmpty(passwordText.textProperty()))
                .or(Bindings.isEmpty(portText.textProperty()))
        );
    }

    @FXML
    public void buttonLoginClick() {
        if (isValid(loginText,Pattern.AUTHORIZATION)&&isValid(passwordText,Pattern.AUTHORIZATION)) {
            String request = String.format("{\"reqCode\":\"authorization\",\"%s\":\"%s\",\"%s\":\"%s\"}"
                    , Authorized.LOGIN, loginText.getText()
                    , Authorized.PASSWORD, passwordText.getText()
            );
            JsonObject object;
            Connection.setPort(Integer.parseInt(portText.getText()));
            Connection.setAddress("localhost");
            if ((object = call(request)) != null) {
                getAuthorizationResult(object);
            }
        }
    }

    @FXML
    public void registerLinkClick() {
        setViewCenter(View.REGISTRATION.toString());
    }

    void getAuthorizationResult(JsonObject object) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Сообщение");

        try {
            if (object.containsKey("code")) {
                alert.setContentText(object.getString("text"));
                alert.showAndWait();

                if (object.getInt("code") != 500) {
                    HomeStage homeStage = new HomeStage();
                    Stage primaryStage = (Stage) buttonLogin.getScene().getWindow();

                    if (object.getInt("code") == 201)
                        homeStage.main(primaryStage, View.ADMIN.toString());
                    else if (object.getInt("code") == 202)
                        homeStage.main(primaryStage, View.STATISTIC.toString());

                    Authorized.setLogin(loginText.getText());
                    Authorized.setPassword(passwordText.getText());
                }
            } else {
                alert.setContentText(object.getString("text"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(e.getMessage());
        }
    }

    public void setViewCenter(String view) {
        Parent root = loadView(view);
        borderPane.setCenter(root);
    }
}