package by.victory.client.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Trip extends Identity {
    public final static String ADDRESS_ID = "address_id";
    public final static String PRODUCT_ID = "product_id";
    public final static String DRIVER_ID = "driver_id";
    public final static String TRANSPORT_ID = "transport_id";
    public final static String ADDRESS = "address";
    public final static String PRODUCT = "product";
    public final static String DRIVER = "driver";
    public final static String TRANSPORT = "transport";
    public final static String DISTANCE = "distance";

    private StringProperty address;
    private StringProperty product;
    private StringProperty driver;
    private StringProperty transport;
    private DoubleProperty distance;

    public Trip() {
        super();
        this.address = new SimpleStringProperty();
        this.product = new SimpleStringProperty();
        this.driver = new SimpleStringProperty();
        this.transport = new SimpleStringProperty();
        this.distance = new SimpleDoubleProperty();
    }

    public Trip(int id, String address, String product, String driver,
                String transport, double distance) {
        super(id);
        this.address.set(address);
        this.product.set(product);
        this.driver.set(driver);
        this.transport.set(transport);
        this.distance.set(distance);
    }

    public String getAddress() {
        return address.get();
    }

    public Trip setAddress(String address) {
        this.address.set(address);
        return this;
    }

    public String getProduct() {
        return product.get();
    }

    public Trip setProduct(String product) {
        this.product.set(product);
        return this;
    }

    public String getDriver() {
        return driver.get();
    }

    public Trip setDriver(String driver) {
        this.driver.set(driver);
        return this;
    }

    public String getTransport() {
        return transport.get();
    }

    public Trip setTransport(String transport) {
        this.transport.set(transport);
        return this;
    }

    public double getDistance() {
        return distance.get();
    }

    public Trip setDistance(double distance) {
        this.distance.set(distance);
        return this;
    }

    public StringProperty getAddressProperty() {
        return address;
    }

    public StringProperty getProductProperty() {
        return product;
    }

    public StringProperty getDriverProperty() {
        return driver;
    }

    public StringProperty getTransportProperty() {
        return transport;
    }

    public DoubleProperty getDistanceProperty() {
        return distance;
    }

    public Trip setId(int id) {
        this.id.set(id);
        return this;
    }
}