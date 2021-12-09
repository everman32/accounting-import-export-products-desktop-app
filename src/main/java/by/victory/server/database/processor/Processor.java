package by.victory.server.database.processor;

import javax.json.JsonObject;
import java.sql.Connection;
import java.util.Map;
import java.util.function.Function;

abstract public class Processor {
    protected static final String ERROR_CREATE="Не удалось создать запись";
    protected static final String ERROR_DELETE="Не удалось удалить запись";
    protected static final String ERROR_UPDATE="Не удалось обновить запись";

    protected static final String SUCCESS_CREATE="Запись создана";
    protected static final String SUCCESS_DELETE="Запись удалена";
    protected static final String SUCCESS_UPDATE="Запись обновлена";

    public static final Map<String, Function<Parameter<JsonObject>, JsonObject>> FUNCTIONS = new java.util.HashMap<>();
    public static class Parameter<T> {
        public final Connection C;
        public final T T;

        public Parameter(Connection c, T t) {
            C = c;
            T = t;
        }
    }
}
