package by.victory.server.database.processor;

import by.victory.client.model.Export;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public class ExportProcessor extends Processor{
    public static Function<Parameter<JsonObject>, JsonObject> get(String reqCode) {
        return FUNCTIONS.get(reqCode);
    }
    static{
        FUNCTIONS.put("getExport", arg -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();

            String query = "SELECT exprt.id, CONCAT(addr.country,', ',addr.postal_code) AS address_destination, " +
                    "CONCAT(prod.name,', ',prod.number, ', ',prod.cost_price) AS product, " +
                    "assmnt.description AS export_assessment, " +
                    "exprt.departure_date FROM export exprt " +
                    "INNER JOIN trip trp ON trp.id=exprt.export_trip_id " +
                    "INNER JOIN assessment assmnt ON assmnt.id=exprt.export_assessment_id " +
                    "INNER JOIN address addr ON addr.id=trp.address_id " +
                    "INNER JOIN product prod ON prod.id=trp.product_id ";

            try(ResultSet result = arg.C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(query)) {
                if(result.first())
                    do {
                        builder.add(
                                Json.createObjectBuilder()
                                        .add(Export.ID, result.getInt(1))
                                        .add(Export.ADDRESS_DESTINATION, result.getString(2))
                                        .add(Export.PRODUCT, result.getString(3))
                                        .add(Export.EXPORT_ASSESSMENT, result.getString(4))
                                        .add(Export.DEPARTURE_DATE, result.getString(5))
                        );
                    } while(result.next());
            } catch(Throwable e) {
                e.printStackTrace();
            }
            return Json.createObjectBuilder().add("data", builder).build();
        });
        FUNCTIONS.put("getExportReport", arg -> {
            final int selected_export_id=arg.T.getInt("id");
            JsonArrayBuilder builder = Json.createArrayBuilder();

            String query = "SELECT exprt.id, " +
                    "CONCAT(addr.country,', ',addr.city,', ',addr.postal_code) AS address_destination, " +
                    "CONCAT(prod.name,', ',prod.number, 'шт., ',prod.cost_price,'у.е за шт.') AS product, " +
                    "CONCAT(drv.first_name,' ', drv.patronymic, ' ', drv.last_name) AS driver, " +
                    "CONCAT(trnsp.brand,' ', trnsp.model, ', ', trnsp.vin_number) AS transport, " +
                    "CONCAT(trp.distance,' км') AS distance, " +
                    "assmnt.description AS export_assessment, exprt.departure_date FROM export exprt " +
                    "INNER JOIN trip trp ON trp.id=exprt.export_trip_id " +
                    "INNER JOIN assessment assmnt ON assmnt.id=exprt.export_assessment_id " +
                    "INNER JOIN address addr ON addr.id=trp.address_id " +
                    "INNER JOIN product prod ON prod.id=trp.product_id " +
                    "INNER JOIN driver drv ON drv.id=trp.driver_id " +
                    "INNER JOIN transport trnsp ON trnsp.id=trp.transport_id " +
                    "WHERE exprt.id=" +selected_export_id;

            try(ResultSet result = arg.C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(query)) {
                if(result.first())
                    do {
                        builder.add(
                                Json.createObjectBuilder()
                                        .add(Export.ID, result.getInt(1))
                                        .add(Export.ADDRESS_DESTINATION, result.getString(2))
                                        .add(Export.PRODUCT, result.getString(3))
                                        .add(Export.DRIVER, result.getString(4))
                                        .add(Export.TRANSPORT, result.getString(5))
                                        .add(Export.DISTANCE, result.getString(6))
                                        .add(Export.EXPORT_ASSESSMENT, result.getString(7))
                                        .add(Export.DEPARTURE_DATE, result.getString(8))
                        );
                    } while(result.next());
            } catch(Throwable e) {
                e.printStackTrace();
            }
            return Json.createObjectBuilder().add("data", builder).build();
        });
        FUNCTIONS.put("createExport", arg -> {
            String query = "INSERT INTO export(export_trip_id, export_assessment_id, departure_date) " +
                    "VALUES(?,?,STR_TO_DATE(?,'%d.%m.%Y'))";

            String text = ERROR_CREATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Export.EXPORT_TRIP_ID));
                pS.setInt(2, arg.T.getInt(Export.EXPORT_ASSESSMENT_ID));
                pS.setString(3, arg.T.getString(Export.DEPARTURE_DATE));
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
        FUNCTIONS.put("deleteExport", arg -> {
            String query = "DELETE FROM export WHERE id= ?";
            String text = ERROR_DELETE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Export.ID));

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
        FUNCTIONS.put("updateExport", arg -> {
            final String COLUMN_NAME=arg.T.getString("column_name").replaceAll("\"","");
            final Object VALUE=arg.T.get("value");

            String query = "UPDATE export SET "+COLUMN_NAME +"="+VALUE+" WHERE id=?";
            String text = ERROR_UPDATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Export.ID));

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
