package by.victory.client.controller;

import by.victory.client.behavior.ComponentBehavior;
import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.model.Trip;
import by.victory.client.validation.DoubleValidateBehavior;
import by.victory.client.validation.Pattern;
import by.victory.client.validation.TextFieldListener;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class TripController implements SocketBehavior, ComponentBehavior, ResponseBehavior, DoubleValidateBehavior {
    protected static final byte[] REQUEST_GET_TRIPS = "{\"reqCode\":\"getTrips\"}".getBytes();
    @FXML
    private TableView<Trip> table;
    @FXML
    private TableColumn<Trip, Integer> idColumn;
    @FXML
    private TableColumn<Trip, String> addressColumn;
    @FXML
    private TableColumn<Trip, String> productColumn;
    @FXML
    private TableColumn<Trip, String> driverColumn;
    @FXML
    private TableColumn<Trip, String> transportColumn;
    @FXML
    private TableColumn<Trip, Double> distanceColumn;
    @FXML
    private ComboBox<JsonObject> recordComboBox;
    @FXML
    private ComboBox<JsonObject> addressComboBox;
    @FXML
    private ComboBox<JsonObject> productComboBox;
    @FXML
    private ComboBox<JsonObject> driverComboBox;
    @FXML
    private ComboBox<JsonObject> transportComboBox;
    @FXML
    private ComboBox<JsonObject> addressComboBoxUpdate;
    @FXML
    private ComboBox<JsonObject> productComboBoxUpdate;
    @FXML
    private ComboBox<JsonObject> driverComboBoxUpdate;
    @FXML
    private ComboBox<JsonObject> transportComboBoxUpdate;
    @FXML
    private TextField distanceText;
    @FXML
    private TextField distanceUpdateText;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonRefresh;
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonClearForm;
    @FXML
    private Button buttonAddressUpdate;
    @FXML
    private Button buttonProductUpdate;
    @FXML
    private Button buttonDriverUpdate;
    @FXML
    private Button buttonTransportUpdate;
    @FXML
    private Button buttonDistanceUpdate;
    @FXML
    private Tab tabAddress;
    @FXML
    private Tab tabProduct;
    @FXML
    private Tab tabDriver;
    @FXML
    private Tab tabTransport;
    @FXML
    private Tab tabDistance;

    @FXML
    void initialize() {
        initTableColumns();

        distanceText.textProperty().addListener(new TextFieldListener(distanceText,
                Pattern.DOUBLE));

        distanceUpdateText.textProperty().addListener(new TextFieldListener(distanceUpdateText,
                Pattern.DOUBLE));

        buttonDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonCreate.disableProperty().bind(Bindings.isNull(addressComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(productComboBox.getSelectionModel().selectedItemProperty()))
                .or(Bindings.isNull(driverComboBox.getSelectionModel().selectedItemProperty()))
                .or(Bindings.isNull(transportComboBox.getSelectionModel().selectedItemProperty()))
                .or(Bindings.isEmpty(distanceText.textProperty()))
        );
        buttonAddressUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(addressComboBoxUpdate.getSelectionModel().selectedItemProperty()))
        );
        buttonProductUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(productComboBoxUpdate.getSelectionModel().selectedItemProperty()))
        );
        buttonDriverUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(driverComboBoxUpdate.getSelectionModel().selectedItemProperty()))
        );
        buttonTransportUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(transportComboBoxUpdate.getSelectionModel().selectedItemProperty()))
        );
        buttonDistanceUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(distanceUpdateText.textProperty()))
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
    public void tabAddressSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        addressComboBoxUpdate.getSelectionModel().clearSelection();
    }

    @FXML
    public void tabProductSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        productComboBoxUpdate.getSelectionModel().clearSelection();
    }

    @FXML
    public void tabDriverSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        driverComboBoxUpdate.getSelectionModel().clearSelection();
    }

    @FXML
    public void tabTransportSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        transportComboBoxUpdate.getSelectionModel().clearSelection();
    }

    @FXML
    public void tabDistanceSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        distanceUpdateText.setText("");
    }

    @FXML
    public void buttonRefreshClick() {
        refreshTable();
    }

    @FXML
    public void buttonDeleteClick() {
        String request = String.format("{\"reqCode\":\"deleteTrip\",\"%s\":%d}"
                , Trip.ID, table.getSelectionModel().getSelectedItem().getId()
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
            refreshTable();
        }
    }

    @FXML
    public void buttonCreateClick() {
        if (isDoubleValid(distanceText)) {
            String request = String.format("{\"reqCode\":\"createTrip\"," +
                            "\"%s\":%d,\"%s\":%d,\"%s\":%d,\"%s\":%d, " +
                            "\"%s\":\"%s\"}"
                    , Trip.ADDRESS_ID, addressComboBox.getSelectionModel().getSelectedItem().getInt("id")
                    , Trip.PRODUCT_ID, productComboBox.getSelectionModel().getSelectedItem().getInt("id")
                    , Trip.DRIVER_ID, driverComboBox.getSelectionModel().getSelectedItem().getInt("id")
                    , Trip.TRANSPORT_ID, transportComboBox.getSelectionModel().getSelectedItem().getInt("id")
                    , Trip.DISTANCE, distanceText.getText()
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
    public void buttonAddressUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateTrip\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Trip.ADDRESS_ID
                , "value", addressComboBoxUpdate.getSelectionModel().getSelectedItem().getInt("id")
                , Trip.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonProductUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateTrip\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Trip.PRODUCT_ID
                , "value", productComboBoxUpdate.getSelectionModel().getSelectedItem().getInt("id")
                , Trip.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonDriverUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateTrip\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Trip.DRIVER_ID
                , "value", driverComboBoxUpdate.getSelectionModel().getSelectedItem().getInt("id")
                , Trip.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonTransportUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateTrip\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Trip.TRANSPORT_ID
                , "value", transportComboBoxUpdate.getSelectionModel().getSelectedItem().getInt("id")
                , Trip.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonDistanceUpdateClick() {
        if (isDoubleValid(distanceUpdateText)) {
            String request = String.format("{\"reqCode\":\"updateTrip\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", Trip.DISTANCE
                    , "value", distanceUpdateText.getText()
                    , Trip.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void recordComboBoxChange() {
        refreshComboBox(REQUEST_GET_TRIPS, recordComboBox);
    }

    @FXML
    public void addressComboBoxChange() {
        refreshComboBox(AddressController.REQUEST_GET_ADDRESSES, addressComboBox);
    }

    @FXML
    public void productComboBoxChange() {
        refreshComboBox(ProductController.REQUEST_GET_PRODUCTS, productComboBox);
    }

    @FXML
    public void driverComboBoxChange() {
        refreshComboBox(DriverController.REQUEST_GET_DRIVERS, driverComboBox);
    }

    @FXML
    public void transportComboBoxChange() {
        refreshComboBox(TransportController.REQUEST_GET_TRANSPORT, transportComboBox);
    }

    @FXML
    public void addressComboBoxUpdateChange() {
        refreshComboBox(AddressController.REQUEST_GET_ADDRESSES, addressComboBoxUpdate);
    }

    @FXML
    public void productComboBoxUpdateChange() {
        refreshComboBox(ProductController.REQUEST_GET_PRODUCTS, productComboBoxUpdate);
    }

    @FXML
    public void driverComboBoxUpdateChange() {
        refreshComboBox(DriverController.REQUEST_GET_DRIVERS, driverComboBoxUpdate);
    }

    @FXML
    public void transportComboBoxUpdateChange() {
        refreshComboBox(TransportController.REQUEST_GET_TRANSPORT, transportComboBoxUpdate);
    }

    public void initTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        productColumn.setCellValueFactory(cellData -> cellData.getValue().getProductProperty());
        driverColumn.setCellValueFactory(cellData -> cellData.getValue().getDriverProperty());
        transportColumn.setCellValueFactory(cellData -> cellData.getValue().getTransportProperty());
        distanceColumn.setCellValueFactory(cellData -> cellData.getValue().getDistanceProperty().asObject());
    }

    public void refreshTable() {
        JsonArray array;
        if ((array = call(REQUEST_GET_TRIPS)) != null) {
            ObservableList<Trip> items = table.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(new Trip()
                        .setId(json.getInt(Trip.ID))
                        .setAddress(json.getString(Trip.ADDRESS))
                        .setProduct(json.getString(Trip.PRODUCT))
                        .setDriver(json.getString(Trip.DRIVER))
                        .setTransport(json.getString(Trip.TRANSPORT))
                        .setDistance(json.getJsonNumber(Trip.DISTANCE).doubleValue())
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
        addressComboBox.getSelectionModel().clearSelection();
        productComboBox.getSelectionModel().clearSelection();
        driverComboBox.getSelectionModel().clearSelection();
        transportComboBox.getSelectionModel().clearSelection();
        distanceText.setText("");
    }

    public void refreshUpdatingForm() {
        recordComboBox.getSelectionModel().clearSelection();
        addressComboBoxUpdate.getSelectionModel().clearSelection();
        productComboBoxUpdate.getSelectionModel().clearSelection();
        driverComboBoxUpdate.getSelectionModel().clearSelection();
        transportComboBoxUpdate.getSelectionModel().clearSelection();
        distanceUpdateText.setText("");
    }
}
