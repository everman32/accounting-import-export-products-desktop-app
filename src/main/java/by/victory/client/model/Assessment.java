package by.victory.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Assessment extends Identity {
    public final static String DESCRIPTION = "description";

    private StringProperty description;

    public Assessment() {
        super();
        this.description = new SimpleStringProperty();
    }

    public Assessment(int id, String description) {
        super(id);
        this.description.set(description);
    }

    public String getDescription() {
        return description.get();
    }

    public Assessment setDescription(String description) {
        this.description.set(description);
        return this;
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public Assessment setId(int id) {
        this.id.set(id);
        return this;
    }
}
