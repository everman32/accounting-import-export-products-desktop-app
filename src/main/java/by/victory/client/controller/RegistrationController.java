package by.victory.client.controller;

import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.behavior.ViewBehavior;
import by.victory.client.model.Authorized;
import by.victory.client.model.Connection;
import by.victory.client.model.Register;
import by.victory.client.stage.HomeStage;
import by.victory.client.stage.View;
import by.victory.client.validation.Pattern;
import by.victory.client.validation.RegexValidateBehavior;
import by.victory.client.validation.TextFieldListener;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.json.JsonObject;
import java.io.IOException;

public class RegistrationController implements ViewBehavior, SocketBehavior, ResponseBehavior,
        RegexValidateBehavior {
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField loginText;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField portText;
    @FXML
    private Button buttonRegister;

    @FXML
    void initialize() {
        loginText.textProperty().addListener(new TextFieldListener(loginText,
                Pattern.AUTHORIZATION));
        passwordText.textProperty().addListener(new TextFieldListener(passwordText,
                Pattern.AUTHORIZATION));
        emailText.textProperty().addListener(new TextFieldListener(emailText,
                Pattern.EMAIL));
        portText.textProperty().addListener(new TextFieldListener(portText,
                Pattern.NUMBER));
        buttonRegister.disableProperty().bind(Bindings.isEmpty(loginText.textProperty())
                .or(Bindings.isEmpty(passwordText.textProperty()))
                .or(Bindings.isEmpty(emailText.textProperty()))
                .or(Bindings.isEmpty(portText.textProperty()))
        );
    }

    @FXML
    public void buttonRegisterClick() {
        if (isValid(loginText,Pattern.AUTHORIZATION)&&isValid(passwordText,Pattern.AUTHORIZATION)&&
                isValid(emailText, Pattern.EMAIL)) {
            String request = String.format("{\"reqCode\":\"registration\",\"%s\":\"%s\",\"%s\":\"%s\", " +
                            "\"%s\":\"%s\"}"
                    , Register.LOGIN, loginText.getText()
                    , Register.PASSWORD, passwordText.getText()
                    , Register.EMAIL, emailText.getText()
            );
            JsonObject object;
            Connection.setPort(Integer.parseInt(portText.getText()));
            Connection.setAddress("localhost");
            if ((object = call(request)) != null) {
                getRegistrationResult(object);
            }
        }
    }

    @FXML
    public void loginLinkClick() {
        setViewCenter(View.AUTHORIZATION.toString());
    }

    void getRegistrationResult(JsonObject object) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Сообщение");

        try {
            //System.out.println("Response: " + object);
            if (object.containsKey("code")) {
                alert.setContentText(object.getString("text"));
                alert.showAndWait();

                if (object.getInt("code") == 200) {
                    HomeStage homeStage = new HomeStage();
                    Stage primaryStage = (Stage) buttonRegister.getScene().getWindow();
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
