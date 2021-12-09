package by.victory.server.database.processor;

import by.victory.client.model.Address;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public class AddressProcessor extends Processor{
    public static Function<Parameter<JsonObject>, JsonObject> get(String reqCode) {
        return FUNCTIONS.get(reqCode);
    }
    static {
        FUNCTIONS.put("getAddresses", arg -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();
            String query = "SELECT * FROM address";

            try (ResultSet result = arg.C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(query)) {
                if (result.first())
                    do {
                        builder.add(
                                Json.createObjectBuilder()
                                        .add(Address.ID, result.getInt(1))
                                        .add(Address.COUNTRY, result.getString(2))
                                        .add(Address.CITY, result.getString(3))
                                        .add(Address.POSTAL_CODE, result.getString(4))
                        );
                    } while (result.next());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return Json.createObjectBuilder().add("data", builder).build();
        });
        FUNCTIONS.put("createAddress", arg -> {
            String query = "INSERT INTO address(country, city, postal_code) VALUES(?,?,?)";
            String text = ERROR_CREATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setString(1, arg.T.getString(Address.COUNTRY));
                pS.setString(2, arg.T.getString(Address.CITY));
                pS.setString(3, arg.T.getString(Address.POSTAL_CODE));
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
        FUNCTIONS.put("deleteAddress", arg -> {
            String query = "DELETE FROM address WHERE id= ?";
            String text = ERROR_DELETE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Address.ID));
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
        FUNCTIONS.put("updateAddress", arg -> {
            final String COLUMN_NAME=arg.T.getString("column_name").replaceAll("\"","");
            final Object VALUE=arg.T.get("value");

            String query = "UPDATE address SET "+COLUMN_NAME +"="+VALUE+" WHERE id=?";
            String text = ERROR_UPDATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Address.ID));
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
