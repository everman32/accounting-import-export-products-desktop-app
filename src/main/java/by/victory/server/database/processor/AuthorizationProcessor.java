package by.victory.server.database.processor;

import by.victory.client.model.Authorized;
import by.victory.client.model.Register;

import javax.json.Json;
import javax.json.JsonObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public class AuthorizationProcessor extends Processor{
    private static final String ERROR_EXECUTE_AUTH ="Не удалось выполнить авторизацию пользователя";
    private static final String ERROR_NOT_FOUND_USER ="Пользователь с таким логином не существует";
    private static final String ERROR_WRONG_PASSWORD ="Введён неверный пароль учётной записи";
    private static final String SUCCESS_AUTH="Авторизация выполнена";
    public static Function<Parameter<JsonObject>, JsonObject> get(String reqCode) {
        return FUNCTIONS.get(reqCode);
    }
    static{
        FUNCTIONS.put("authorization", arg -> {
            String query = "SELECT login FROM user WHERE login= ?";
            String text;
            int code = 500;

            try(PreparedStatement firstPS = arg.C.prepareStatement(query)) {
                firstPS.setString(1, arg.T.getString(Authorized.LOGIN));

                ResultSet firstResult = firstPS.executeQuery();
                if(firstResult.isBeforeFirst()) {
                    query = "SELECT role_id, login, password FROM user WHERE login= ? AND password= ?";
                    text = ERROR_WRONG_PASSWORD;

                    try (PreparedStatement secondPS = arg.C.prepareStatement(query)) {
                        secondPS.setString(1, arg.T.getString(Register.LOGIN));
                        secondPS.setString(2, arg.T.getString(Register.PASSWORD));

                        ResultSet secondResult = secondPS.executeQuery();
                        if(secondResult.isBeforeFirst()) {
                            while(secondResult.next()) {
                                int role_id = secondResult.getInt("role_id");
                                if (role_id == 1) code = 201;
                                else if (role_id == 2) code = 202;
                            }
                            text = SUCCESS_AUTH+". Вы вошли как "+arg.T.getString(Authorized.LOGIN);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        text = ERROR_EXECUTE_AUTH +". "+e.getMessage();
                    }
                } else {
                    text = ERROR_NOT_FOUND_USER;
                }
            } catch (Throwable e){
                e.printStackTrace();
                text = ERROR_EXECUTE_AUTH +". "+e.getMessage();
            }
            return Json.createObjectBuilder()
                    .add("code", code)
                    .add("text", text)
                    .build();
        });
        /*
          FUNCTIONS.put("authorization", arg -> {
            String query = "SELECT role_id, login, password FROM user WHERE login= ? AND password= ?";
            String text = "Не удалось найти пользователя с такими учётными данными";
            int code = 500;

            try(PreparedStatement pS = arg.C.prepareStatement(query)) {
                pS.setString(1, arg.T.getString(Auth.LOGIN));
                pS.setString(2, arg.T.getString(Auth.PASSWORD));

                ResultSet result = pS.executeQuery();
                if(result.isBeforeFirst()) {
                    while(result.next()) {
                        int role_id = result.getInt("role_id");
                        if (role_id == 1) code = 201;
                        else if (role_id == 2) code = 202;
                    }
                    text = "Авторизация выполнена. Вы вошли как " +
                            arg.T.getString(Auth.LOGIN);
                }
            }
            catch (Throwable e){
                e.printStackTrace();
            }
            return Json.createObjectBuilder()
                    .add("code", code)
                    .add("text", text)
                    .build();
        });
        */
    }
}