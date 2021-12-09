package by.victory.server.database.processor;

import by.victory.client.model.Driver;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public class DriverProcessor extends Processor{
    public static Function<Parameter<JsonObject>, JsonObject> get(String reqCode) {
        return FUNCTIONS.get(reqCode);
    }
    static{
        FUNCTIONS.put("getDrivers", arg -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();
            String query = "SELECT * FROM driver";
            try(ResultSet result = arg.C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(query)) {
                if(result.first())
                    do {
                        builder.add(
                                Json.createObjectBuilder()
                                        .add(Driver.ID, result.getInt(1))
                                        .add(Driver.FIRST_NAME, result.getString(2))
                                        .add(Driver.PATRONYMIC, result.getString(3))
                                        .add(Driver.LAST_NAME, result.getString(4))
                                        .add(Driver.PHONE_NUMBER, result.getString(5))
                                        .add(Driver.EXPERIENCE, result.getDouble(6))
                        );
                    } while(result.next());
            } catch(Throwable e) {
                e.printStackTrace();
            }
            return Json.createObjectBuilder().add("data", builder).build();
        });
        FUNCTIONS.put("createDriver", arg -> {
            String query = "INSERT INTO driver(first_name, patronymic, last_name, " +
                    "phone_number,experience) VALUES(?,?,?,?,?)";
            String text = ERROR_CREATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setString(1, arg.T.getString(Driver.FIRST_NAME));
                pS.setString(2, arg.T.getString(Driver.PATRONYMIC));
                pS.setString(3, arg.T.getString(Driver.LAST_NAME));
                pS.setString(4, arg.T.getString(Driver.PHONE_NUMBER));
                pS.setDouble(5, Double.parseDouble(arg.T.getString(Driver.EXPERIENCE)));
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
        FUNCTIONS.put("deleteDriver", arg -> {
            String query = "DELETE FROM driver WHERE id= ?";
            String text = ERROR_DELETE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Driver.ID));

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
        FUNCTIONS.put("updateDriver", arg -> {
            final String COLUMN_NAME=arg.T.getString("column_name").replaceAll("\"","");
            final Object VALUE=arg.T.get("value");

            String query = "UPDATE driver SET "+COLUMN_NAME +"="+VALUE+" WHERE id=?";
            String text = ERROR_UPDATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Driver.ID));

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
