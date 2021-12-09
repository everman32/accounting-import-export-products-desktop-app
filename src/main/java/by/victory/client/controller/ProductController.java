package by.victory.client.controller;

import by.victory.client.behavior.ComponentBehavior;
import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.model.Product;
import by.victory.client.validation.DoubleValidateBehavior;
import by.victory.client.validation.NumberValidateBehavior;
import by.victory.client.validation.Pattern;
import by.victory.client.validation.TextFieldListener;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class ProductController implements SocketBehavior, ComponentBehavior, ResponseBehavior,
        NumberValidateBehavior, DoubleValidateBehavior {
    protected static final byte[] REQUEST_GET_PRODUCTS = "{\"reqCode\":\"getProducts\"}".getBytes();
    @FXML
    private TableView<Product> table;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Integer> numberColumn;
    @FXML
    private TableColumn<Product, Double> costPriceColumn;
    @FXML
    private ComboBox<JsonObject> recordComboBox;
    @FXML
    private TextField nameText;
    @FXML
    private TextField numberText;
    @FXML
    private TextField costPriceText;
    @FXML
    private TextField nameUpdateText;
    @FXML
    private TextField numberUpdateText;
    @FXML
    private TextField costPriceUpdateText;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonRefresh;
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonClearForm;
    @FXML
    private Button buttonNameUpdate;
    @FXML
    private Button buttonNumberUpdate;
    @FXML
    private Button buttonCostPriceUpdate;
    @FXML
    private Tab tabName;
    @FXML
    private Tab tabNumber;
    @FXML
    private Tab tabCostPrice;
    @FXML
    private BarChart<String, Double> costPriceDiagram;

    @FXML
    void initialize() {
        initTableColumns();

        nameText.textProperty().addListener(new TextFieldListener(nameText,
                Pattern.NAME));
        numberText.textProperty().addListener(new TextFieldListener(numberText,
                Pattern.NUMBER));
        costPriceText.textProperty().addListener(new TextFieldListener(costPriceText,
                Pattern.DOUBLE));

        nameUpdateText.textProperty().addListener(new TextFieldListener(nameUpdateText,
                Pattern.NAME));
        numberUpdateText.textProperty().addListener(new TextFieldListener(numberUpdateText,
                Pattern.NUMBER));
        costPriceUpdateText.textProperty().addListener(new TextFieldListener(costPriceUpdateText,
                Pattern.DOUBLE));

        buttonDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonCreate.disableProperty().bind(Bindings.isEmpty(nameText.textProperty())
                .or(Bindings.isEmpty(numberText.textProperty()))
                .or(Bindings.isEmpty(costPriceText.textProperty()))
        );
        buttonNameUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(nameUpdateText.textProperty()))
        );
        buttonNumberUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(numberUpdateText.textProperty()))
        );
        buttonCostPriceUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(costPriceUpdateText.textProperty()))
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
    public void tabNameSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        nameUpdateText.setText("");
    }

    @FXML
    public void tabNumberSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        numberUpdateText.setText("");
    }

    @FXML
    public void tabCostPriceSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        costPriceUpdateText.setText("");
    }

    @FXML
    public void tabCostPriceDiagramSelect() {
        costPriceDiagram.getData().clear();
        ObservableList<Product> items = table.getItems();
        XYChart.Series<String, Double> series =new XYChart.Series<>();

        for (Product item : items) {
            series.getData().add(new XYChart.Data<>(item.getName(), item.getCost_price()));
        }
        costPriceDiagram.getData().add(series);
    }

    @FXML
    public void buttonRefreshClick() {
        refreshTable();
    }

    @FXML
    public void buttonDeleteClick() {
        String request = String.format("{\"reqCode\":\"deleteProduct\",\"%s\":%d}"
                , Product.ID, table.getSelectionModel().getSelectedItem().getId()
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
            refreshTable();
        }
    }

    @FXML
    public void buttonCreateClick() {
        if (isNumberValid(numberText)&&isDoubleValid(costPriceText)) {
            String request = String.format("{\"reqCode\":\"createProduct\"," +
                            "\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}"
                    , Product.NAME, nameText.getText()
                    , Product.NUMBER, numberText.getText()
                    , Product.COST_PRICE, costPriceText.getText()
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
    public void buttonNameUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateProduct\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Product.NAME
                , "value", nameUpdateText.getText()
                , Product.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonNumberUpdateClick() {
        if (isNumberValid(numberUpdateText)) {
            String request = String.format("{\"reqCode\":\"updateProduct\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", Product.NUMBER
                    , "value", numberUpdateText.getText()
                    , Product.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void buttonCostPriceUpdateClick() {
        if (isDoubleValid(costPriceUpdateText)) {
            String request = String.format("{\"reqCode\":\"updateProduct\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", Product.COST_PRICE
                    , "value", costPriceUpdateText.getText()
                    , Product.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void recordComboBoxChange() {
        refreshComboBox(REQUEST_GET_PRODUCTS, recordComboBox);
    }

    public void initTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberProperty().asObject());
        costPriceColumn.setCellValueFactory(cellData -> cellData.getValue().getCostPriceProperty().asObject());
    }

    public void refreshTable() {
        JsonArray array;
        if ((array = call(REQUEST_GET_PRODUCTS)) != null) {
            ObservableList<Product> items = table.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(new Product()
                        .setId(json.getInt(Product.ID))
                        .setName(json.getString(Product.NAME))
                        .setNumber(json.getInt(Product.NUMBER))
                        .setCost_price(json.getJsonNumber(Product.COST_PRICE).doubleValue())
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
        nameText.setText("");
        numberText.setText("");
        costPriceText.setText("");
    }

    public void refreshUpdatingForm() {
        recordComboBox.getSelectionModel().clearSelection();
        nameUpdateText.setText("");
        numberUpdateText.setText("");
        costPriceUpdateText.setText("");
    }
}
