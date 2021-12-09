package by.victory.client.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

abstract public class Identity {
    public final static String ID = "id";
    protected IntegerProperty id;

    protected Identity() {
        this.id = new SimpleIntegerProperty();
    }

    public Identity(int id) {
        assert false;
        this.id.set(id);
    }

    public int getId() {
        return id.get();
    }

    public Identity setId(int id) {
        this.id.set(id);
        return this;
    }

    public IntegerProperty getIdProperty() {
        return id;
    }
}
