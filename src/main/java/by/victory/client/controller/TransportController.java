package by.victory.client.controller;

import by.victory.client.behavior.ComponentBehavior;
import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.model.Transport;
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

public class TransportController implements SocketBehavior, ComponentBehavior, ResponseBehavior,
        RegexValidateBehavior {
    protected static final byte[] REQUEST_GET_TRANSPORT = "{\"reqCode\":\"getTransport\"}".getBytes();
    @FXML
    private TableView<Transport> table;
    @FXML
    private TableColumn<Transport, Integer> idColumn;
    @FXML
    private TableColumn<Transport, String> brandColumn;
    @FXML
    private TableColumn<Transport, String> modelColumn;
    @FXML
    private TableColumn<Transport, String> vinColumn;
    @FXML
    private ComboBox<JsonObject> recordComboBox;
    @FXML
    private TextField brandText;
    @FXML
    private TextField modelText;
    @FXML
    private TextField vinText;
    @FXML
    private TextField brandUpdateText;
    @FXML
    private TextField modelUpdateText;
    @FXML
    private TextField vinUpdateText;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonRefresh;
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonClearForm;
    @FXML
    private Button buttonBrandUpdate;
    @FXML
    private Button buttonModelUpdate;
    @FXML
    private Button buttonVinUpdate;
    @FXML
    private Tab tabBrand;
    @FXML
    private Tab tabModel;
    @FXML
    private Tab tabVin;

    @FXML
    void initialize() {
        initTableColumns();

        brandText.textProperty().addListener(new TextFieldListener(brandText,
                Pattern.NAME));
        modelText.textProperty().addListener(new TextFieldListener(modelText,
                Pattern.NAME));
        vinText.textProperty().addListener(new TextFieldListener(vinText,
                Pattern.VIN));

        brandUpdateText.textProperty().addListener(new TextFieldListener(brandUpdateText,
                Pattern.NAME));
        modelUpdateText.textProperty().addListener(new TextFieldListener(modelUpdateText,
                Pattern.NAME));
        vinUpdateText.textProperty().addListener(new TextFieldListener(vinUpdateText,
                Pattern.VIN));

        buttonDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonCreate.disableProperty().bind(Bindings.isEmpty(brandText.textProperty())
                .or(Bindings.isEmpty(modelText.textProperty()))
                .or(Bindings.isEmpty(vinText.textProperty()))
        );
        buttonBrandUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(brandUpdateText.textProperty()))
        );
        buttonModelUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(modelUpdateText.textProperty()))
        );
        buttonVinUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(vinUpdateText.textProperty()))
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
    public void tabBrandSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        brandUpdateText.setText("");
    }

    @FXML
    public void tabModelSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        modelUpdateText.setText("");
    }

    @FXML
    public void tabVinSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        vinUpdateText.setText("");
    }

    @FXML
    public void buttonRefreshClick() {
        refreshTable();
    }

    @FXML
    public void buttonDeleteClick() {
        String request = String.format("{\"reqCode\":\"deleteTransport\",\"%s\":%d}"
                , Transport.ID, table.getSelectionModel().getSelectedItem().getId()
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
            refreshTable();
        }
    }

    @FXML
    public void buttonCreateClick() {
        if (isValid(vinText, Pattern.VIN)) {
            String request = String.format("{\"reqCode\":\"createTransport\"," +
                            "\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}"
                    , Transport.BRAND, brandText.getText()
                    , Transport.MODEL, modelText.getText()
                    , Transport.VIN, vinText.getText()
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
    public void buttonBrandUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateTransport\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Transport.BRAND
                , "value", brandUpdateText.getText()
                , Transport.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonModelUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateTransport\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Transport.MODEL
                , "value", modelUpdateText.getText()
                , Transport.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonVinUpdateClick() {
        if (isValid(vinUpdateText, Pattern.VIN)) {
            String request = String.format("{\"reqCode\":\"updateTransport\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", Transport.VIN
                    , "value", vinUpdateText.getText()
                    , Transport.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void recordComboBoxChange() {
        refreshComboBox(REQUEST_GET_TRANSPORT, recordComboBox);
    }

    public void initTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().getBrandProperty());
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().getModelProperty());
        vinColumn.setCellValueFactory(cellData -> cellData.getValue().getVinProperty());
    }

    public void refreshTable() {
        JsonArray array;
        if ((array = call(REQUEST_GET_TRANSPORT)) != null) {
            ObservableList<Transport> items = table.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(new Transport()
                        .setId(json.getInt(Transport.ID))
                        .setBrand(json.getString(Transport.BRAND))
                        .setModel(json.getString(Transport.MODEL))
                        .setVin(json.getString(Transport.VIN))
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
        brandText.setText("");
        modelText.setText("");
        vinText.setText("");
    }

    public void refreshUpdatingForm() {
        recordComboBox.getSelectionModel().clearSelection();
        brandUpdateText.setText("");
        modelUpdateText.setText("");
        vinUpdateText.setText("");
    }
}
