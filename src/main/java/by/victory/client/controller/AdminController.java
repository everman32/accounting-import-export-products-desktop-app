package by.victory.client.controller;

import by.victory.client.behavior.ViewBehavior;
import by.victory.client.stage.View;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class AdminController implements ViewBehavior {
    @FXML
    BorderPane borderPane;
    @FXML
    private Button buttonUser;
    @FXML
    private Button buttonExport;
    @FXML
    private Button buttonImport;
    @FXML
    private Button buttonTrip;
    @FXML
    private Button buttonAddress;
    @FXML
    private Button buttonTransport;
    @FXML
    private Button buttonProduct;
    @FXML
    private Button buttonDriver;

    @FXML
    public void buttonUserClick() {
        setViewCenter(View.USER.toString());
    }

    @FXML
    public void buttonExportClick() {
        setViewCenter(View.EXPORT.toString());
    }

    @FXML
    public void buttonImportClick() {
        setViewCenter(View.IMPORT.toString());
    }

    @FXML
    public void buttonTripClick() {
        setViewCenter(View.TRIP.toString());
    }

    @FXML
    public void buttonAddressClick() {
        setViewCenter(View.ADDRESS.toString());
    }

    @FXML
    public void buttonTransportClick() {
        setViewCenter(View.TRANSPORT.toString());
    }

    @FXML
    public void buttonDriverClick() {
        setViewCenter(View.DRIVER.toString());
    }

    @FXML
    public void buttonProductClick() {
        setViewCenter(View.PRODUCT.toString());
    }

    public void setViewCenter(String view) {
        Parent root = loadView(view);
        borderPane.setCenter(root);
    }
}
