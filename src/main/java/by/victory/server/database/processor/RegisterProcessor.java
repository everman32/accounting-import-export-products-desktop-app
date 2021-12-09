package by.victory.server.database.processor;

import by.victory.client.model.Register;

import javax.json.Json;
import javax.json.JsonObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public class RegisterProcessor extends Processor {
    private static final String ERROR_EXECUTE_REGISTER ="Не удалось выполнить регистрацию";
    private static final String ERROR_SAME_REGISTER ="Пользователь с введённым данными уже существуют. " +
            "Логин и эл. почта должны быть уникальными";
    private static final String SUCCESS_REGISTER="Регистрация выполнена";
    public static Function<Parameter<JsonObject>, JsonObject> get(String reqCode) {
        return FUNCTIONS.get(reqCode);
    }
    static {
        FUNCTIONS.put("registration", arg -> {
            String query = "SELECT login FROM user WHERE login= ? OR email= ?";
            String text= ERROR_EXECUTE_REGISTER;
            int code = 500;

            try (PreparedStatement readPS = arg.C.prepareStatement(query)) {
                readPS.setString(1, arg.T.getString(Register.LOGIN));
                readPS.setString(2, arg.T.getString(Register.EMAIL));
                ResultSet result = readPS.executeQuery();
                if (!result.isBeforeFirst()) {
                    query = "INSERT INTO user(login, password, email) VALUES(?,?,?)";

                    try (PreparedStatement createPS = arg.C.prepareStatement(query)) {
                        createPS.setString(1, arg.T.getString(Register.LOGIN));
                        createPS.setString(2, arg.T.getString(Register.PASSWORD));
                        createPS.setString(3, arg.T.getString(Register.EMAIL));
                        if (createPS.executeUpdate() > 0) {
                            code = 200;
                            text = SUCCESS_REGISTER+". Вы вошли как "+arg.T.getString(Register.LOGIN);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        text = ERROR_EXECUTE_REGISTER +". "+e.getMessage();
                    }
                } else {
                    text = ERROR_SAME_REGISTER;
                }
            } catch (Throwable e) {
                e.printStackTrace();
                text = ERROR_EXECUTE_REGISTER+". "+e.getMessage();
            }
            return Json.createObjectBuilder()
                    .add("code", code)
                    .add("text", text)
                    .build();
        });
    }
}
