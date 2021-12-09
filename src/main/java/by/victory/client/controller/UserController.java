package by.victory.client.controller;

import by.victory.client.behavior.ComponentBehavior;
import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.model.Authorized;
import by.victory.client.model.User;
import by.victory.client.validation.Pattern;
import by.victory.client.validation.RegexValidateBehavior;
import by.victory.client.validation.TextFieldListener;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class UserController implements SocketBehavior, ComponentBehavior, ResponseBehavior, RegexValidateBehavior {
    private static final byte[] REQUEST_GET_USERS = "{\"reqCode\":\"getUsers\"}".getBytes();
    private static final byte[] REQUEST_GET_ROLES = "{\"reqCode\":\"getRoles\"}".getBytes();
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> loginColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private ComboBox<JsonObject> recordComboBox;
    @FXML
    private ComboBox<JsonObject> roleComboBox;
    @FXML
    private ComboBox<JsonObject> roleComboBoxUpdate;
    @FXML
    private TextField loginText;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField loginUpdateText;
    @FXML
    private TextField passwordUpdateText;
    @FXML
    private TextField emailUpdateText;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonRefresh;
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonClearForm;
    @FXML
    private Button buttonRoleUpdate;
    @FXML
    private Button buttonLoginUpdate;
    @FXML
    private Button buttonPasswordUpdate;
    @FXML
    private Button buttonEmailUpdate;
    @FXML
    private Tab tabRole;
    @FXML
    private Tab tabLogin;
    @FXML
    private Tab tabPassword;
    @FXML
    private Tab tabEmail;

    @FXML
    void initialize() {
        initTableColumns();

        loginText.textProperty().addListener(new TextFieldListener(loginText,
                Pattern.AUTHORIZATION));
        passwordText.textProperty().addListener(new TextFieldListener(passwordText,
                Pattern.AUTHORIZATION));
        emailText.textProperty().addListener(new TextFieldListener(emailText,
                Pattern.EMAIL));

        loginUpdateText.textProperty().addListener(new TextFieldListener(loginUpdateText,
                Pattern.AUTHORIZATION));
        passwordUpdateText.textProperty().addListener(new TextFieldListener(passwordUpdateText,
                Pattern.AUTHORIZATION));
        emailUpdateText.textProperty().addListener(new TextFieldListener(emailUpdateText,
                Pattern.EMAIL));

        buttonDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonCreate.disableProperty().bind(Bindings.isNull(roleComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(loginText.textProperty()))
                .or(Bindings.isEmpty(passwordText.textProperty()))
                .or(Bindings.isEmpty(emailText.textProperty()))
        );

        buttonRoleUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(roleComboBoxUpdate.getSelectionModel().selectedItemProperty()))
        );
        buttonLoginUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(loginUpdateText.textProperty()))
        );
        buttonPasswordUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(passwordUpdateText.textProperty()))
        );
        buttonEmailUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(emailUpdateText.textProperty()))
        );
    }

    @FXML
    public void tabReadSelect() {
        refreshTable();
    }

    @FXML
    public void tabCreateSelect() {
        refreshCreatingForm();
    }

    @FXML
    public void tabUpdateSelect() {
        refreshUpdatingForm();
    }

    @FXML
    public void tabRoleSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        roleComboBoxUpdate.getSelectionModel().clearSelection();
    }

    @FXML
    public void tabLoginSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        loginUpdateText.setText("");
    }

    @FXML
    public void tabPasswordSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        passwordUpdateText.setText("");
    }

    @FXML
    public void tabEmailSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        emailUpdateText.setText("");
    }

    @FXML
    public void buttonRefreshClick() {
        refreshTable();
    }

    @FXML
    public void buttonDeleteClick() {
        if (!table.getSelectionModel().getSelectedItem().getLogin().equals(Authorized.getLogin())) {
            String request = String.format("{\"reqCode\":\"deleteUser\",\"%s\":%d}"
                    , User.ID, table.getSelectionModel().getSelectedItem().getId()
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
                refreshTable();
            }
        } else showAlert("Нельзя удалить учётную запись с которой выполнен вход");
    }

    @FXML
    public void buttonCreateClick() {
        if (isValid(loginText, Pattern.AUTHORIZATION)&&isValid(passwordText, Pattern.AUTHORIZATION)&&
                isValid(emailText,Pattern.EMAIL)) {
            String request = String.format("{\"reqCode\":\"createUser\"," +
                            "\"%s\":%d,\"%s\":\"%s\",\"%s\":\"%s\"," +
                            "\"%s\":\"%s\"}"
                    , User.ROLE_ID, roleComboBox.getSelectionModel().getSelectedItem().getInt("id")
                    , User.LOGIN, loginText.getText()
                    , User.PASSWORD, passwordText.getText()
                    , User.EMAIL, emailText.getText()
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void buttonClearFormClick() {
        refreshCreatingForm();
    }

    @FXML
    public void buttonRoleUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateUser\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", User.ROLE_ID
                , "value", roleComboBoxUpdate.getSelectionModel().getSelectedItem().getInt("id")
                , User.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonLoginUpdateClick() {
        if (isValid(loginUpdateText, Pattern.AUTHORIZATION)) {
            String request = String.format("{\"reqCode\":\"updateUser\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", User.LOGIN
                    , "value", loginUpdateText.getText()
                    , User.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void buttonPasswordUpdateClick() {
        if (isValid(passwordUpdateText,Pattern.AUTHORIZATION)) {
            String request = String.format("{\"reqCode\":\"updateUser\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", User.PASSWORD
                    , "value", passwordUpdateText.getText()
                    , User.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void buttonEmailUpdateClick() {
        if (isValid(emailUpdateText, Pattern.EMAIL)) {
            String request = String.format("{\"reqCode\":\"updateUser\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", User.EMAIL
                    , "value", emailUpdateText.getText()
                    , User.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void roleComboBoxChange() {
        refreshComboBox(REQUEST_GET_ROLES, roleComboBox);
    }

    @FXML
    public void roleComboBoxUpdateChange() {
        refreshComboBox(REQUEST_GET_ROLES, roleComboBoxUpdate);
    }

    @FXML
    public void recordComboBoxChange() {
        refreshComboBox(REQUEST_GET_USERS, recordComboBox);
    }

    public void initTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        loginColumn.setCellValueFactory(cellData -> cellData.getValue().getLoginProperty());
        passwordColumn.setCellValueFactory(cellData -> cellData.getValue().getPasswordProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
    }

    public void refreshTable() {
        JsonArray array;
        if ((array = call(REQUEST_GET_USERS)) != null) {
            ObservableList<User> items = table.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(new User()
                        .setId(json.getInt(User.ID))
                        .setType(json.getString(User.TYPE))
                        .setLogin(json.getString(User.LOGIN))
                        .setPassword(json.getString(User.PASSWORD))
                        .setEmail(json.getString(User.EMAIL))
                );
            }
        }
    }

    public void refreshComboBox(byte[] request, ComboBox<JsonObject> comboBox) {
        JsonArray array;
        if ((array = call(request)) != null) {
            ObservableList<JsonObject> items = comboBox.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(json);
            }
        }
    }

    public void refreshCreatingForm() {
        roleComboBox.getSelectionModel().clearSelection();
        loginText.setText("");
        passwordText.setText("");
        emailText.setText("");
    }

    public void refreshUpdatingForm() {
        recordComboBox.getSelectionModel().clearSelection();
        roleComboBoxUpdate.getSelectionModel().clearSelection();
        loginUpdateText.setText("");
        passwordUpdateText.setText("");
        emailUpdateText.setText("");
    }
}
