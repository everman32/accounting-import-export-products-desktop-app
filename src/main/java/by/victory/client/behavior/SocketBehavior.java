package by.victory.client.behavior;


import by.victory.client.model.Connection;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.net.Socket;

public interface SocketBehavior extends AlertBehavior{
    default JsonArray call(byte[] request) {
        JsonArray array = null;
        try (Socket socket = new Socket(Connection.getAddress(),Connection.getPort())) {
            socket.getOutputStream().write(request);
            JsonObject object = Json.createReader(socket.getInputStream()).readObject();

            if (object.containsKey("data"))
                array = object.getJsonArray("data");
        } catch (Throwable e) {
            e.printStackTrace();
            showAlert(e.getMessage());
        }
        return array;
    }

    default JsonObject call(String request) {
        JsonObject object = null;
        try (Socket socket = new Socket(Connection.getAddress(),Connection.getPort())) {
            socket.getOutputStream().write(request.getBytes());
            object = Json.createReader(socket.getInputStream()).readObject();
        } catch (Throwable e) {
            e.printStackTrace();
            showAlert(e.getMessage());
        }
        return object;
    }
}
