package org.zells.qi.node.fakes;

import org.zells.qi.node.connecting.Channel;

import java.util.HashMap;
import java.util.Map;

public class FakeChannel implements Channel {

    public String sent;

    private Channel.SignalListener listener;
    private String connection;

    public static Map<String, FakeChannel> channels = new HashMap<>();

    public FakeChannel(String connection) {
        this.connection = connection;
        channels.put(connection, this);
    }

    public void receive(String signal) {
        sent = listener.receives(signal);
    }

    @Override
    public String send(String signal) {
        sent = signal;
        return "OK";
    }

    @Override
    public void listen(SignalListener listener) {
        this.listener = listener;
    }

    @Override
    public void close() {
    }

    @Override
    public String getConnection() {
        return connection;
    }

    public static void reset() {
        channels.clear();
    }
}
