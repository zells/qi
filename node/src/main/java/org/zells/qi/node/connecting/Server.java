package org.zells.qi.node.connecting;

public interface Server {

    void start(SignalListener listener);

    void stop();

    String getConnection();

    interface SignalListener {
        Signal receives(Signal signal);
    }
}
