package by.victory.server.database.processor;

import by.victory.client.model.Trip;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public class TripProcessor extends Processor{
    public static Function<Parameter<JsonObject>, JsonObject> get(String reqCode) {
        return FUNCTIONS.get(reqCode);
    }
    static{
        FUNCTIONS.put("getTrips", arg -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();
            String query = "SELECT trp.id, CONCAT(addr.country,', ',addr.postal_code) AS address, " +
                    "CONCAT(prod.name,', ',prod.number) AS product, " +
                    "CONCAT(driv.first_name,' ',driv.patronymic,' ',driv.last_name) AS  driver, " +
                    "CONCAT(transp.brand,' ',transp.model) AS transport, " +
                    "trp.distance FROM trip trp " +
                    "INNER JOIN address addr ON addr.id=trp.address_id " +
                    "INNER JOIN product prod ON prod.id=trp.product_id " +
                    "INNER JOIN driver  driv ON driv.id=trp.driver_id " +
                    "INNER JOIN transport transp ON transp.id=trp.transport_id";

            try(ResultSet result = arg.C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(query)) {
                if(result.first())
                    do {
                        builder.add(
                                Json.createObjectBuilder()
                                        .add(Trip.ID, result.getInt(1))
                                        .add(Trip.ADDRESS, result.getString(2))
                                        .add(Trip.PRODUCT, result.getString(3))
                                        .add(Trip.DRIVER, result.getString(4))
                                        .add(Trip.TRANSPORT, result.getString(5))
                                        .add(Trip.DISTANCE, result.getDouble(6))
                        );
                    } while(result.next());
            } catch(Throwable e) {
                e.printStackTrace();
            }
            return Json.createObjectBuilder().add("data", builder).build();
        });
        FUNCTIONS.put("createTrip", arg -> {
            String query = "INSERT INTO trip(address_id, product_id, driver_id, transport_id, " +
                    "distance) VALUES(?,?,?,?,?)";
            String text = ERROR_CREATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Trip.ADDRESS_ID));
                pS.setInt(2, arg.T.getInt(Trip.PRODUCT_ID));
                pS.setInt(3, arg.T.getInt(Trip.DRIVER_ID));
                pS.setInt(4, arg.T.getInt(Trip.TRANSPORT_ID));
                pS.setDouble(5, Double.parseDouble(arg.T.getString(Trip.DISTANCE)));
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
        FUNCTIONS.put("deleteTrip", arg -> {
            String query = "DELETE FROM trip WHERE id= ?";
            String text = ERROR_DELETE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Trip.ID));
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
        FUNCTIONS.put("updateTrip", arg -> {
            final String COLUMN_NAME=arg.T.getString("column_name").replaceAll("\"","");
            final Object VALUE=arg.T.get("value");

            String query = "UPDATE trip SET "+COLUMN_NAME +"="+VALUE+" WHERE id=?";
            String text = ERROR_UPDATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Trip.ID));

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
