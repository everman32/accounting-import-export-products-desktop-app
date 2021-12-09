package by.victory.server.database.processor;

import by.victory.client.model.Assessment;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.sql.ResultSet;
import java.util.function.Function;

public class AssessmentProcessor extends Processor{
    public static Function<Parameter<JsonObject>, JsonObject> get(String reqCode) {
        return FUNCTIONS.get(reqCode);
    }
    static {
        FUNCTIONS.put("getAssessments", arg -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();
            String query = "SELECT * FROM assessment";

            try (ResultSet result = arg.C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(query)) {
                if (result.first())
                    do {
                        builder.add(
                                Json.createObjectBuilder()
                                        .add(Assessment.ID, result.getInt(1))
                                        .add(Assessment.DESCRIPTION, result.getString(2))
                        );
                    } while (result.next());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return Json.createObjectBuilder().add("data", builder).build();
        });
    }
}
