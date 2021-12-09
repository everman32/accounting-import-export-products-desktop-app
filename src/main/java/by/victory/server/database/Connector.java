package by.victory.server.database;

import by.victory.server.TaskProvider;
import by.victory.server.database.processor.*;

import javax.json.Json;
import javax.json.JsonObject;
import java.net.Socket;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class Connector implements Runnable, AutoCloseable {
   private final AtomicBoolean CLOSED = new AtomicBoolean();
    private final Connection C;
    private TaskProvider holder;
    private Socket client;
    private boolean active;

    public Connector(String database, String username, String password)  throws Throwable {
        C = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, username, password);
    }

    public void start(Socket client, TaskProvider holder) {
        if(holder == null)
            throw new UnsupportedOperationException("Требуется владелец этого коннектора");
        synchronized(C) {
            if(active)
                throw new UnsupportedOperationException("Этот коннектор запущен");
            if(client == null || client.isClosed())
                throw new IllegalArgumentException("Срок действия этого клиента истёк");
            this.client = client;
            this.holder = holder;
            C.notify();
            active = !active;
        }
    }

    @Override
    public void run() {
        for(;;) {
            try {
                synchronized(C) {
                    active = false;
                    C.wait();
                }
            } catch(Throwable e) {
                break;
            }
            System.out.println("Новый клиент: " + client.getRemoteSocketAddress());

            JsonObject json;
            try {
                Function<Processor.Parameter<JsonObject>, JsonObject> f;
                json = Json.createReader(client.getInputStream()).readObject();
                System.out.println("Запрос: " + json);
                if(!json.containsKey("reqCode") ||
                        ((f = AuthorizationProcessor.get(json.getString("reqCode"))) == null
                                &&(f = RegisterProcessor.get(json.getString("reqCode"))) == null
                                &&(f = ProductProcessor.get(json.getString("reqCode"))) == null
                                &&(f = DriverProcessor.get(json.getString("reqCode"))) == null
                                &&(f = TransportProcessor.get(json.getString("reqCode"))) == null
                                &&(f = AddressProcessor.get(json.getString("reqCode"))) == null
                                &&(f = TripProcessor.get(json.getString("reqCode"))) == null
                                &&(f = ExportProcessor.get(json.getString("reqCode"))) == null
                                &&(f = ImportProcessor.get(json.getString("reqCode"))) == null
                                &&(f = AssessmentProcessor.get(json.getString("reqCode"))) == null
                                &&(f = UserProcessor.get(json.getString("reqCode"))) == null
                                &&(f = RoleProcessor.get(json.getString("reqCode"))) == null
                ))
                    throw new UnsupportedOperationException("Не найден код запроса");
                json = f.apply(new Processor.Parameter<>(C, json));
            } catch(Throwable e) {
                json = Json.createObjectBuilder()
                        .add("code", 400)
                        .add("text", "Bad Request")
                        .add("reason", e.getMessage()).build();
            }
            System.out.println("Ответ: "+json);
            try {
                Json.createWriter(client.getOutputStream()).writeObject(json);
                client.close();
            } catch(Throwable e) {
                e.printStackTrace();
            }

            while(!holder.putBack(this));
            if(CLOSED.get()) break;
        }
    }

    @Override
    public void close() {
        try { C.close(); }
        catch(Throwable e) {
            e.printStackTrace();
            return;
        }
        CLOSED.set(true);
    }

    public final boolean isActived() {
        synchronized(C) {
            return active;
        }
    }

    public final boolean isClosed() { return CLOSED.get(); }

}