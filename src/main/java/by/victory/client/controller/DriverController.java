package by.victory.client.controller;

import by.victory.client.behavior.ComponentBehavior;
import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.model.Driver;
import by.victory.client.validation.DoubleValidateBehavior;
import by.victory.client.validation.Pattern;
import by.victory.client.validation.RegexValidateBehavior;
import by.victory.client.validation.TextFieldListener;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class DriverController implements SocketBehavior, ComponentBehavior, ResponseBehavior, RegexValidateBehavior,
        DoubleValidateBehavior {
    protected static final byte[] REQUEST_GET_DRIVERS = "{\"reqCode\":\"getDrivers\"}".getBytes();
    @FXML
    private TableView<Driver> table;
    @FXML
    private TableColumn<Driver, Integer> idColumn;
    @FXML
    private TableColumn<Driver, String> firstNameColumn;
    @FXML
    private TableColumn<Driver, String> patronymicColumn;
    @FXML
    private TableColumn<Driver, String> lastNameColumn;
    @FXML
    private TableColumn<Driver, String> phoneNumberColumn;
    @FXML
    private TableColumn<Driver, Double> experienceColumn;
    @FXML
    private ComboBox<JsonObject> recordComboBox;
    @FXML
    private TextField firstNameText;
    @FXML
    private TextField patronymicText;
    @FXML
    private TextField lastNameText;
    @FXML
    private TextField phoneNumberText;
    @FXML
    private TextField experienceText;
    @FXML
    private TextField firstNameUpdateText;
    @FXML
    private TextField patronymicUpdateText;
    @FXML
    private TextField lastNameUpdateText;
    @FXML
    private TextField phoneNumberUpdateText;
    @FXML
    private TextField experienceUpdateText;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonRefresh;
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonClearForm;
    @FXML
    private Button buttonFirstNameUpdate;
    @FXML
    private Button buttonPatronymicUpdate;
    @FXML
    private Button buttonLastNameUpdate;
    @FXML
    private Button buttonPhoneNumberUpdate;
    @FXML
    private Button buttonExperienceUpdate;
    @FXML
    private Tab tabFirstName;
    @FXML
    private Tab tabPatronymic;
    @FXML
    private Tab tabLastName;
    @FXML
    private Tab tabPhoneNumber;
    @FXML
    private Tab tabExperience;
    @FXML
    private Tab tabExperienceGraph;
    @FXML
    private LineChart<String, Double> experienceGraph;

    @FXML
    void initialize() {
        initTableColumns();

        firstNameText.textProperty().addListener(new TextFieldListener(firstNameText,
                Pattern.PERSON_DATA));
        patronymicText.textProperty().addListener(new TextFieldListener(patronymicText,
                Pattern.PERSON_DATA));
        lastNameText.textProperty().addListener(new TextFieldListener(lastNameText,
                Pattern.PERSON_DATA));
        phoneNumberText.textProperty().addListener(new TextFieldListener(phoneNumberText,
                Pattern.PHONE_NUMBER));
        experienceText.textProperty().addListener(new TextFieldListener(experienceText,
                Pattern.DOUBLE));

        firstNameUpdateText.textProperty().addListener(new TextFieldListener(firstNameUpdateText,
                Pattern.PERSON_DATA));
        patronymicUpdateText.textProperty().addListener(new TextFieldListener(patronymicUpdateText,
                Pattern.PERSON_DATA));
        lastNameUpdateText.textProperty().addListener(new TextFieldListener(lastNameUpdateText,
                Pattern.PERSON_DATA));
        phoneNumberUpdateText.textProperty().addListener(new TextFieldListener(phoneNumberUpdateText,
                Pattern.PHONE_NUMBER));
        experienceUpdateText.textProperty().addListener(new TextFieldListener(experienceUpdateText,
                Pattern.DOUBLE));

        buttonDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonCreate.disableProperty().bind(Bindings.isEmpty(firstNameText.textProperty())
                .or(Bindings.isEmpty(patronymicText.textProperty()))
                .or(Bindings.isEmpty(lastNameText.textProperty()))
                .or(Bindings.isEmpty(phoneNumberText.textProperty()))
                .or(Bindings.isEmpty(experienceText.textProperty()))
        );
        buttonFirstNameUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(firstNameUpdateText.textProperty()))
        );
        buttonPatronymicUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(patronymicUpdateText.textProperty()))
        );
        buttonLastNameUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(lastNameUpdateText.textProperty()))
        );
        buttonPhoneNumberUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(phoneNumberUpdateText.textProperty()))
        );
        buttonExperienceUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(experienceUpdateText.textProperty()))
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
    public void tabFirstNameSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        firstNameUpdateText.setText("");
    }

    @FXML
    public void tabPatronymicSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        patronymicUpdateText.setText("");
    }

    @FXML
    public void tabLastNameSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        lastNameUpdateText.setText("");
    }

    @FXML
    public void tabPhoneNumberSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        phoneNumberUpdateText.setText("");
    }

    @FXML
    public void tabExperienceSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        experienceUpdateText.setText("");
    }

    @FXML
    public void tabExperienceGraphSelect() {
        experienceGraph.getData().clear();
        ObservableList<Driver> items = table.getItems();
        XYChart.Series<String, Double> series =new XYChart.Series<>();

        for (Driver item : items) {
            series.getData().add(new XYChart.Data<>(item.getLast_name()+" "+item.getFirst_name().charAt(0)+". "+
                    item.getPatronymic().charAt(0)+".", item.getExperience()));
        }
        experienceGraph.getData().add(series);
    }

    @FXML
    public void buttonRefreshClick() {
        refreshTable();
    }

    @FXML
    public void buttonDeleteClick() {
        String request = String.format("{\"reqCode\":\"deleteDriver\",\"%s\":%d}"
                , Driver.ID, table.getSelectionModel().getSelectedItem().getId()
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
            refreshTable();
        }
    }

    @FXML
    public void buttonCreateClick() {
        if (isValid(phoneNumberText,Pattern.PHONE_NUMBER)&&isDoubleValid(experienceText)) {
            String request = String.format("{\"reqCode\":\"createDriver\"," +
                            "\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"," +
                            "\"%s\":\"%s\",\"%s\":\"%s\"}"
                    , Driver.FIRST_NAME, firstNameText.getText()
                    , Driver.PATRONYMIC, patronymicText.getText()
                    , Driver.LAST_NAME, lastNameText.getText()
                    , Driver.PHONE_NUMBER, phoneNumberText.getText()
                    , Driver.EXPERIENCE, experienceText.getText()
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
    public void buttonFirstNameUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateDriver\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Driver.FIRST_NAME
                , "value", firstNameUpdateText.getText()
                , Driver.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonPatronymicUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateDriver\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Driver.PATRONYMIC
                , "value", patronymicUpdateText.getText()
                , Driver.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonLastNameUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateDriver\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Driver.LAST_NAME
                , "value", lastNameUpdateText.getText()
                , Driver.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonPhoneNumberUpdateClick() {
        if (isValid(phoneNumberUpdateText, Pattern.PHONE_NUMBER)) {
            String request = String.format("{\"reqCode\":\"updateDriver\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", Driver.PHONE_NUMBER
                    , "value", phoneNumberUpdateText.getText()
                    , Driver.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void buttonExperienceUpdateClick() {
        if (isDoubleValid(experienceUpdateText)) {
            String request = String.format("{\"reqCode\":\"updateDriver\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", Driver.EXPERIENCE
                    , "value", experienceUpdateText.getText()
                    , Driver.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void recordComboBoxChange() {
        refreshComboBox(REQUEST_GET_DRIVERS, recordComboBox);
    }

    public void initTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
        patronymicColumn.setCellValueFactory(cellData -> cellData.getValue().getPatronymicProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());
        phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumberProperty());
        experienceColumn.setCellValueFactory(cellData -> cellData.getValue().getExperienceProperty().asObject());
    }

    public void refreshTable() {
        JsonArray array;
        if ((array = call(REQUEST_GET_DRIVERS)) != null) {
            ObservableList<Driver> items = table.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(new Driver()
                        .setId(json.getInt(Driver.ID))
                        .setFirst_name(json.getString(Driver.FIRST_NAME))
                        .setPatronymic(json.getString(Driver.PATRONYMIC))
                        .setLast_name(json.getString(Driver.LAST_NAME))
                        .setPhone_number(json.getString(Driver.PHONE_NUMBER))
                        .setExperience(json.getJsonNumber(Driver.EXPERIENCE).doubleValue())
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
        firstNameText.setText("");
        patronymicText.setText("");
        lastNameText.setText("");
        phoneNumberText.setText("");
        experienceText.setText("");
    }

    public void refreshUpdatingForm() {
        recordComboBox.getSelectionModel().clearSelection();
        firstNameUpdateText.setText("");
        patronymicUpdateText.setText("");
        lastNameUpdateText.setText("");
        phoneNumberUpdateText.setText("");
        experienceUpdateText.setText("");
    }
}
