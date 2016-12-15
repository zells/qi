package org.zells.qi.node.fakes;

import org.zells.qi.node.connecting.Server;
import org.zells.qi.node.singalling.Signal;

public class FakeServer implements Server {

    private SignalListener listener;

    public Signal responded;

    private String connection;

    public FakeServer(String connection) {
        this.connection = connection;
    }

    public void receive(Signal signal) {
        responded = listener.receives(signal);
    }

    @Override
    public String getConnection() {
        return connection;
    }

    @Override
    public void start(SignalListener listener) {
        this.listener = listener;
    }

    @Override
    public void stop() {
    }
}
