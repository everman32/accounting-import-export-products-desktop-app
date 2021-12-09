package by.victory.client.behavior;

import javax.json.JsonObject;

public interface ResponseBehavior extends AlertBehavior {
    default void showContentResponse(JsonObject object) {
        showAlert(object.getString("text"));
    }
}
