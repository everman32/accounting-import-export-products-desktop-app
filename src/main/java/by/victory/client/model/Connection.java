package by.victory.client.model;

public class Connection {
    private static int port;
    private static String address;

    public Connection(int port, String address) {
        Connection.port = port;
        Connection.address = address;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Connection.port = port;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Connection.address = address;
    }
}
