package by.victory.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Role extends Identity {
    public final static String TYPE = "type";

    private StringProperty type;

    public Role() {
        super();
        this.type = new SimpleStringProperty();
    }

    public Role(int id, String type) {
        super(id);
        this.type.set(type);
    }

    public String getType() {
        return type.get();
    }

    public Role setType(String type) {
        this.type.set(type);
        return this;
    }

    public StringProperty getTypeProperty() {
        return type;
    }

    public Role setId(int id) {
        this.id.set(id);
        return this;
    }
}
