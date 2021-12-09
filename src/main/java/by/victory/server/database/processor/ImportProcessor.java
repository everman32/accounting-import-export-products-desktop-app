package by.victory.server.database.processor;

import by.victory.client.model.Import;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public class ImportProcessor extends Processor{
    public static Function<Parameter<JsonObject>, JsonObject> get(String reqCode) {
        return FUNCTIONS.get(reqCode);
    }
    static{
        FUNCTIONS.put("getImport", arg -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();

            String query = "SELECT imprt.id, CONCAT(addr.country,', ',addr.postal_code) AS address_source, " +
                    "CONCAT(prod.name,', ',prod.number, ', ',prod.cost_price) AS product, " +
                    "assmnt.description AS import_assessment, " +
                    "imprt.arrival_date FROM import imprt " +
                    "INNER JOIN trip trp ON trp.id=imprt.import_trip_id " +
                    "INNER JOIN assessment assmnt ON assmnt.id=imprt.import_assessment_id " +
                    "INNER JOIN address addr ON addr.id=trp.address_id " +
                    "INNER JOIN product prod ON prod.id=trp.product_id ";

            try(ResultSet result = arg.C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(query)) {
                if(result.first())
                    do {
                        builder.add(
                                Json.createObjectBuilder()
                                        .add(Import.ID, result.getInt(1))
                                        .add(Import.ADDRESS_SOURCE, result.getString(2))
                                        .add(Import.PRODUCT, result.getString(3))
                                        .add(Import.IMPORT_ASSESSMENT, result.getString(4))
                                        .add(Import.ARRIVAL_DATE, result.getString(5))
                        );
                    } while(result.next());
            } catch(Throwable e) {
                e.printStackTrace();
            }
            return Json.createObjectBuilder().add("data", builder).build();
        });
        FUNCTIONS.put("getImportReport", arg -> {
            final int selected_import_id=arg.T.getInt("id");
            JsonArrayBuilder builder = Json.createArrayBuilder();

            String query = "SELECT imprt.id, " +
                    "CONCAT(addr.country,', ',addr.city,', ',addr.postal_code) AS address_source, " +
                    "CONCAT(prod.name,', ',prod.number, 'шт., ',prod.cost_price,'у.е за шт.') AS product, " +
                    "CONCAT(drv.first_name,' ', drv.patronymic, ' ', drv.last_name) AS driver, " +
                    "CONCAT(trnsp.brand,' ', trnsp.model, ', ', trnsp.vin_number) AS transport, " +
                    "CONCAT(trp.distance,' км') AS distance, " +
                    "assmnt.description AS import_assessment, imprt.arrival_date FROM import imprt " +
                    "INNER JOIN trip trp ON trp.id=imprt.import_trip_id " +
                    "INNER JOIN assessment assmnt ON assmnt.id=imprt.import_assessment_id " +
                    "INNER JOIN address addr ON addr.id=trp.address_id " +
                    "INNER JOIN product prod ON prod.id=trp.product_id " +
                    "INNER JOIN driver drv ON drv.id=trp.driver_id " +
                    "INNER JOIN transport trnsp ON trnsp.id=trp.transport_id " +
                    "WHERE imprt.id=" +selected_import_id;

            try(ResultSet result = arg.C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(query)) {
                if(result.first())
                    do {
                        builder.add(
                                Json.createObjectBuilder()
                                        .add(Import.ID, result.getInt(1))
                                        .add(Import.ADDRESS_SOURCE, result.getString(2))
                                        .add(Import.PRODUCT, result.getString(3))
                                        .add(Import.DRIVER, result.getString(4))
                                        .add(Import.TRANSPORT, result.getString(5))
                                        .add(Import.DISTANCE, result.getString(6))
                                        .add(Import.IMPORT_ASSESSMENT, result.getString(7))
                                        .add(Import.ARRIVAL_DATE, result.getString(8))
                        );
                    } while(result.next());
            } catch(Throwable e) {
                e.printStackTrace();
            }
            return Json.createObjectBuilder().add("data", builder).build();
        });
        FUNCTIONS.put("createImport", arg -> {
            String query = "INSERT INTO import(import_trip_id, import_assessment_id, arrival_date) " +
                    "VALUES(?,?,STR_TO_DATE(?,'%d.%m.%Y'))";

            String text = ERROR_CREATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Import.IMPORT_TRIP_ID));
                pS.setInt(2, arg.T.getInt(Import.IMPORT_ASSESSMENT_ID));
                pS.setString(3, arg.T.getString(Import.ARRIVAL_DATE));
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
        FUNCTIONS.put("deleteImport", arg -> {
            String query = "DELETE FROM import WHERE id= ?";
            String text = ERROR_DELETE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Import.ID));
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
        FUNCTIONS.put("updateImport", arg -> {
            final String COLUMN_NAME=arg.T.getString("column_name").replaceAll("\"","");
            final Object VALUE=arg.T.get("value");

            String query = "UPDATE import SET "+COLUMN_NAME +"="+VALUE+" WHERE id=?";

            String text = ERROR_UPDATE;
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setInt(1, arg.T.getInt(Import.ID));
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