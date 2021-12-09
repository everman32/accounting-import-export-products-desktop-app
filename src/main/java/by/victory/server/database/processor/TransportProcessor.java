package by.victory.server.database.processor;

import by.victory.client.model.Transport;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public class TransportProcessor extends Processor{
    public static Function<Parameter<JsonObject>, JsonObject> get(String reqCode) {
        return FUNCTIONS.get(reqCode);
    }
    static{
        FUNCTIONS.put("getTransport", arg -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();
            String query = "SELECT * FROM transport";

            try(ResultSet result = arg.C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(query)) {
                if(result.first())
                    do {
                        builder.add(
                                Json.createObjectBuilder()
                                        .add(Transport.ID, result.getInt(1))
                                        .add(Transport.BRAND, result.getString(2))
                                        .add(Transport.MODEL, result.getString(3))
                                        .add(Transport.VIN, result.getString(4))
                        );
                    } while(result.next());
            } catch(Throwable e) {
                e.printStackTrace();
            }
            return Json.createObjectBuilder().add("data", builder).build();
        });
        FUNCTIONS.put("createTransport", arg -> {
            String query = "INSERT INTO transport(brand, model, vin_number) VALUES(?,?,?)";
            String text = ERROR_CREATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setString(1, arg.T.getString(Transport.BRAND));
                pS.setString(2, arg.T.getString(Transport.MODEL));
                pS.setString(3, arg.T.getString(Transport.VIN));
                if(pS.executeUpdate() > 0) {
                    code = 200;
                    text = SUCCESS_CREATE;
                }
            } catch(Throwable e) {
                e.printStackTrace();
                text=ERROR_CREATE+". "+e.getMessage();
            }
            return Json.createObjectBuilder()
                    .add("code", code)
                    .add("text", text)
                    .build();
        });
        FUNCTIONS.put("deleteTransport", arg -> {
            String query = "DELETE FROM transport WHERE id= ?";
            String text = ERROR_DELETE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Transport.ID));
                if(pS.executeUpdate() > 0) {
                    text = SUCCESS_DELETE;
                }
            } catch (Throwable e){
                e.printStackTrace();
                text=ERROR_DELETE+". "+e.getMessage();
            }
            return Json.createObjectBuilder()
                    .add("code", code)
                    .add("text", text)
                    .build();
        });
        FUNCTIONS.put("updateTransport", arg -> {
            final String COLUMN_NAME=arg.T.getString("column_name").replaceAll("\"","");
            final Object VALUE=arg.T.get("value");

            String query = "UPDATE transport SET "+COLUMN_NAME +"="+VALUE+" WHERE id=?";
            String text = ERROR_UPDATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Transport.ID));
                if(pS.executeUpdate() > 0) {
                    code = 200;
                    text = SUCCESS_UPDATE;
                }
            } catch(Throwable e) {
                e.printStackTrace();
                text=ERROR_UPDATE+". "+e.getMessage();
            }
            return Json.createObjectBuilder()
                    .add("code", code)
                    .add("text", text)
                    .build();
        });
    }
}
