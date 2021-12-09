package by.victory.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transport extends Identity {
    public final static String BRAND = "brand";
    public final static String MODEL = "model";
    public final static String VIN = "vin_number";

    private StringProperty brand;
    private StringProperty model;
    private StringProperty vin;

    public Transport() {
        super();
        this.brand = new SimpleStringProperty();
        this.model = new SimpleStringProperty();
        this.vin = new SimpleStringProperty();
    }

    public Transport(int id, String brand, String model, String vin) {
        super(id);
        this.brand.set(brand);
        this.model.set(model);
        this.vin.set(vin);
    }

    public String getBrand() {
        return brand.get();
    }

    public Transport setBrand(String brand) {
        this.brand.set(brand);
        return this;
    }

    public String getModel() {
        return model.get();
    }

    public Transport setModel(String model) {
        this.model.set(model);
        return this;
    }

    public String getVin() {
        return vin.get();
    }

    public Transport setVin(String vin) {
        this.vin.set(vin);
        return this;
    }

    public StringProperty getBrandProperty() {
        return brand;
    }

    public StringProperty getModelProperty() {
        return model;
    }

    public StringProperty getVinProperty() {
        return vin;
    }

    public Transport setId(int id) {
        this.id.set(id);
        return this;
    }
}