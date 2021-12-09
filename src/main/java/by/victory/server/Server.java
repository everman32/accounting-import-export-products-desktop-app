package by.victory.server;

import by.victory.server.database.Connector;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            final int port;
            port=args.length>0?Integer.parseInt(args[0]):987;
            new Server(port).start();
        }
        catch(Throwable e) {
            e.printStackTrace();
        }
    }

    private static final String DATABASE = "export_import";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Victor1509";
    public static final long TIMEOUT = 1;//A minute.
    private static final byte[] RESPONSE_TIMEOUT = "{\"code\":429,\"text\":\"Слишком много запросов\"".getBytes();

    private ServerSocket server;
    private Runner runner;
    private final int port;

    public Server(int port) {
        if((this.port = port) < 0 || port > 0xFFFF)
            throw new IllegalArgumentException("Значение порта выходит за диапазон допустимых значений: " + port);
    }

    public void start() throws IOException {
        if(server == null) {
            server = new ServerSocket();
            server.bind(new InetSocketAddress("localhost", port));
            runner = new Runner(server);
            runner.start();
            System.out.println("Сервер запущен и прослушивает порт "+port+"...");
        } else
            throw new IllegalArgumentException("Сервер уже запущен");
    }
    public void close() throws Exception {
        if(!server.isClosed()) {
            runner.close();
            server.close();
            server = null;
            System.out.println("Сервер закрыт");
        } else
            throw new IllegalArgumentException("Сервер уже закрыт");
    }
    private static class Runner implements Runnable, AutoCloseable {
        private final TaskProvider PROVIDER = new TaskProvider(5, DATABASE, USERNAME, PASSWORD);
        private final ServerSocket S;
        private boolean up;
        private Thread t;

        private Runner(ServerSocket server) { S = server; }

        public void run() {
            Connector c;
            for(;;) {
                try {
                    Socket client = S.accept();
                    if((c = PROVIDER.request(TIMEOUT)) == null) {
                        try(OutputStream out = client.getOutputStream()) {
                            out.write(RESPONSE_TIMEOUT);
                            out.flush();
                        }
                        continue;
                    }
                    c.start(client, PROVIDER);
                }
                catch(Throwable e) { break; }
                synchronized(PROVIDER) {
                    if(!up) break;
                }
            }
        }

        public void start() {
            synchronized(PROVIDER) {
                if(!up) {
                    up = true;
                    (t = new Thread(this)).start();
                }
            }
        }

        public void close() {
            synchronized(PROVIDER) {
                if(up) {
                    PROVIDER.close();
                    t.interrupt();
                    t = null;
                    up = false;
                }
            }
        }
    }
}