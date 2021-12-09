package by.victory.client.model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Address extends Identity {
    public final static String COUNTRY = "country";
    public final static String CITY = "city";
    public final static String POSTAL_CODE = "postal_code";

    private StringProperty country;
    private StringProperty city;
    private StringProperty postal_code;

    public Address() {
        super();
        this.country = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.postal_code = new SimpleStringProperty();
    }

    public Address(int id, String country, String city, String postal_code) {
        super(id);
        this.country.set(country);
        this.city.set(city);
        this.postal_code.set(postal_code);
    }

    public String getCountry() {
        return country.get();
    }

    public Address setCountry(String country) {
        this.country.set(country);
        return this;
    }

    public String getCity() {
        return city.get();
    }

    public Address setCity(String city) {
        this.city.set(city);
        return this;
    }

    public String getPostal_code() {
        return postal_code.get();
    }

    public Address setPostal_code(String postal_code) {
        this.postal_code.set(postal_code);
        return this;
    }

    public StringProperty getCountryProperty() {
        return country;
    }

    public StringProperty getCityProperty() {
        return city;
    }

    public StringProperty getPostalCodeProperty() {
        return postal_code;
    }

    public Address setId(int id) {
        this.id.set(id);
        return this;
    }
}
