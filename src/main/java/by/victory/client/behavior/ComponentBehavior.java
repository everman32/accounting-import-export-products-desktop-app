package by.victory.client.behavior;

import javafx.scene.control.ComboBox;

import javax.json.JsonObject;

public interface ComponentBehavior {
    void initTableColumns();

    void refreshTable();

    void refreshComboBox(byte[] request, ComboBox<JsonObject> comboBox);

    void refreshCreatingForm();

    void refreshUpdatingForm();
}
