package by.victory.client.model;

import javafx.beans.property.*;

public class Product extends Identity {
    public final static String NAME = "name";
    public final static String NUMBER = "number";
    public final static String COST_PRICE = "cost_price";

    private StringProperty name;
    private IntegerProperty number;
    private DoubleProperty cost_price;

    public Product() {
        super();
        this.name = new SimpleStringProperty();
        this.number = new SimpleIntegerProperty();
        this.cost_price = new SimpleDoubleProperty();
    }

    public Product(int id, String name, int number, double cost_price) {
        super(id);
        this.name.set(name);
        this.number.set(number);
        this.cost_price.set(cost_price);
    }

    public String getName() {
        return name.get();
    }

    public Product setName(String name) {
        this.name.set(name);
        return this;
    }

    public int getNumber() {
        return number.get();
    }

    public Product setNumber(int number) {
        this.number.set(number);
        return this;
    }

    public double getCost_price() {
        return cost_price.get();
    }

    public Product setCost_price(double cost_price) {
        this.cost_price.set(cost_price);
        return this;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public IntegerProperty getNumberProperty() {
        return number;
    }

    public DoubleProperty getCostPriceProperty() {
        return cost_price;
    }

    public Product setId(int id) {
        this.id.set(id);
        return this;
    }
}