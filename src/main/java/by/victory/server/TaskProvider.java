package by.victory.server;



import by.victory.server.database.Connector;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class TaskProvider implements java.io.Closeable {
    private final BlockingQueue<Connector> RUNNERS;

    public TaskProvider(int capacity, String database, String username, String password) {
        RUNNERS = new ArrayBlockingQueue<>(capacity);
        for(int i = -1; ++i < capacity; ) {
            String name = Connector.class.getSimpleName() + "-" + i;
            Connector connector;
            try {
                connector = new Connector(database, username, password);
            } catch(Throwable e) {
                System.out.format("Создание коннектора %s не удалось (%s)%n", name, e.getMessage());
                continue;
            }
            RUNNERS.add(connector);
            new Thread(connector).start();
        }
        if(RUNNERS.size() < 1)
            throw new UnsupportedOperationException("Коннектор недоступен");
    }
    /**
     * @param timeout Time runs out in seconds.
     * @return Instance of Connector or {@code null} if none is available.
     */
    public Connector request(long timeout) {
        try { return RUNNERS.poll(timeout, TimeUnit.SECONDS); }
        catch(Throwable e) {
            return null;
        }
    }

    public boolean putBack(Connector c) {
        //Putting the Connector back to the queue is always close to success (always true).
        //Because it can't be created carelessly (the constructor is private).
        boolean b = true;
        try { RUNNERS.add(c); }
        catch(Throwable e) {
            b = false;
        }
        return b;
    }

    @Override
    public void close() {
        for(Connector c : RUNNERS)
            c.close();
    }
}