package by.victory.client.model;

public class Authorized {
    public final static String ID = "login";
    public final static String LOGIN = "login";
    public final static String PASSWORD = "password";

    private static int id;
    private static String login;
    private static String password;

    public Authorized(int id, String login, String password) {
        Authorized.id = id;
        Authorized.login = login;
        Authorized.password = password;
    }


    public static void setId(int id) {
        Authorized.id = id;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Authorized.login = login;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Authorized.password = password;
    }
}
