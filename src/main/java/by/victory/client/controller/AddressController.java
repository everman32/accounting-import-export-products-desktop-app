package by.victory.client.controller;

import by.victory.client.behavior.ComponentBehavior;
import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.model.Address;
import by.victory.client.validation.Pattern;
import by.victory.client.validation.TextFieldListener;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class AddressController implements SocketBehavior, ComponentBehavior, ResponseBehavior {
    protected static final byte[] REQUEST_GET_ADDRESSES = "{\"reqCode\":\"getAddresses\"}".getBytes();
    @FXML
    private TableView<Address> table;
    @FXML
    private TableColumn<Address, Integer> idColumn;
    @FXML
    private TableColumn<Address, String> countryColumn;
    @FXML
    private TableColumn<Address, String> cityColumn;
    @FXML
    private TableColumn<Address, String> postalCodeColumn;
    @FXML
    private ComboBox<JsonObject> recordComboBox;
    @FXML
    private TextField countryText;
    @FXML
    private TextField cityText;
    @FXML
    private TextField postalCodeText;
    @FXML
    private TextField countryUpdateText;
    @FXML
    private TextField cityUpdateText;
    @FXML
    private TextField postalCodeUpdateText;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonRefresh;
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonClearForm;
    @FXML
    private Button buttonCountryUpdate;
    @FXML
    private Button buttonCityUpdate;
    @FXML
    private Button buttonPostalCodeUpdate;
    @FXML
    private Tab tabCountry;
    @FXML
    private Tab tabCity;
    @FXML
    private Tab tabPostalCode;

    @FXML
    void initialize() {
        initTableColumns();

        countryText.textProperty().addListener(new TextFieldListener(countryText,
                Pattern.SPACE_DATA));
        cityText.textProperty().addListener(new TextFieldListener(cityText,
                Pattern.SPACE_DATA));
        postalCodeText.textProperty().addListener(new TextFieldListener(postalCodeText,
                Pattern.POSTAL_CODE));

        countryUpdateText.textProperty().addListener(new TextFieldListener(countryUpdateText,
                Pattern.SPACE_DATA));
        cityUpdateText.textProperty().addListener(new TextFieldListener(cityUpdateText,
                Pattern.SPACE_DATA));
        postalCodeUpdateText.textProperty().addListener(new TextFieldListener(postalCodeUpdateText,
                Pattern.POSTAL_CODE));

        buttonDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonCreate.disableProperty().bind(Bindings.isEmpty(countryText.textProperty())
                .or(Bindings.isEmpty(cityText.textProperty()))
                .or(Bindings.isEmpty(postalCodeText.textProperty()))
        );
        buttonCountryUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(countryUpdateText.textProperty()))
        );
        buttonCityUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(cityUpdateText.textProperty()))
        );
        buttonPostalCodeUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(postalCodeUpdateText.textProperty()))
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
    public void tabCountrySelect() {
        recordComboBox.getSelectionModel().clearSelection();
        countryUpdateText.setText("");
    }

    @FXML
    public void tabCitySelect() {
        recordComboBox.getSelectionModel().clearSelection();
        cityUpdateText.setText("");
    }

    @FXML
    public void tabPostalCodeSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        postalCodeUpdateText.setText("");
    }

    @FXML
    public void buttonPostalCodeUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateAddress\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Address.POSTAL_CODE
                , "value", postalCodeUpdateText.getText()
                , Address.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonCountryUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateAddress\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Address.COUNTRY
                , "value", countryUpdateText.getText()
                , Address.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonRefreshClick() {
        refreshTable();
    }

    @FXML
    public void buttonDeleteClick() {
        String request = String.format("{\"reqCode\":\"deleteAddress\",\"%s\":%d}"
                , Address.ID, table.getSelectionModel().getSelectedItem().getId()
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
            refreshTable();
        }
    }

    @FXML
    public void buttonCreateClick() {
        String request = String.format("{\"reqCode\":\"createAddress\"," +
                        "\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}"
                , Address.COUNTRY, countryText.getText()
                , Address.CITY, cityText.getText()
                , Address.POSTAL_CODE, postalCodeText.getText()
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonCityUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateAddress\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Address.CITY
                , "value", cityUpdateText.getText()
                , Address.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonClearFormClick() {
        refreshCreatingForm();
    }

    @FXML
    public void recordComboBoxChange() {
        refreshComboBox(REQUEST_GET_ADDRESSES, recordComboBox);
    }

    public void initTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().getCountryProperty());
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().getCityProperty());
        postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getPostalCodeProperty());
    }

    public void refreshTable() {
        JsonArray array;
        if ((array = call(REQUEST_GET_ADDRESSES)) != null) {
            ObservableList<Address> items = table.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(new Address()
                        .setId(json.getInt(Address.ID))
                        .setCountry(json.getString(Address.COUNTRY))
                        .setCity(json.getString(Address.CITY))
                        .setPostal_code(json.getString(Address.POSTAL_CODE))
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
        countryText.setText("");
        cityText.setText("");
        postalCodeText.setText("");
    }

    public void refreshUpdatingForm() {
        recordComboBox.getSelectionModel().clearSelection();
        countryUpdateText.setText("");
        cityUpdateText.setText("");
        postalCodeUpdateText.setText("");
    }
}
