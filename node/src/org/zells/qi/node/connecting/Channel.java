package org.zells.qi.node.connecting;

public interface Channel {

    String send(String signal);

    void listen(SignalListener listener);

    void close();

    String getConnection();

    interface SignalListener {
        String receives(String signal);
    }
}
