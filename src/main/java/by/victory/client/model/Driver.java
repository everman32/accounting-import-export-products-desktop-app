package by.victory.client.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Driver extends Identity {
    public final static String FIRST_NAME = "first_name";
    public final static String PATRONYMIC = "patronymic";
    public final static String LAST_NAME = "last_name";
    public final static String PHONE_NUMBER = "phone_number";
    public final static String EXPERIENCE = "experience";

    private StringProperty first_name;
    private StringProperty patronymic;
    private StringProperty last_name;
    private StringProperty phone_number;
    private DoubleProperty experience;

    public Driver() {
        super();
        this.first_name = new SimpleStringProperty();
        this.patronymic = new SimpleStringProperty();
        this.last_name = new SimpleStringProperty();
        this.phone_number = new SimpleStringProperty();
        this.experience = new SimpleDoubleProperty();
    }

    public Driver(int id, String first_name, String patronymic, String last_name,
                  String phone_number, double experience) {
        super(id);
        this.first_name.set(first_name);
        this.patronymic.set(patronymic);
        this.last_name.set(last_name);
        this.phone_number.set(phone_number);
        this.experience.set(experience);
    }

    public String getFirst_name() {
        return first_name.get();
    }

    public Driver setFirst_name(String first_name) {
        this.first_name.set(first_name);
        return this;
    }

    public String getPatronymic() {
        return patronymic.get();
    }

    public Driver setPatronymic(String patronymic) {
        this.patronymic.set(patronymic);
        return this;
    }

    public String getLast_name() {
        return last_name.get();
    }

    public Driver setLast_name(String last_name) {
        this.last_name.set(last_name);
        return this;
    }

    public String getPhone_number() {
        return phone_number.get();
    }

    public Driver setPhone_number(String phone_number) {
        this.phone_number.set(phone_number);
        return this;
    }

    public double getExperience() {
        return experience.get();
    }

    public Driver setExperience(double experience) {
        this.experience.set(experience);
        return this;
    }

    public StringProperty getFirstNameProperty() {
        return first_name;
    }

    public StringProperty getPatronymicProperty() {
        return patronymic;
    }

    public StringProperty getLastNameProperty() {
        return last_name;
    }

    public StringProperty getPhoneNumberProperty() {
        return phone_number;
    }

    public DoubleProperty getExperienceProperty() {
        return experience;
    }

    public Driver setId(int id) {
        this.id.set(id);
        return this;
    }
}