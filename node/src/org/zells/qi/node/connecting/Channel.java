package org.zells.qi.node.connecting;

public interface Channel {

    Signal send(String signal);

    void listen(SignalListener listener);

    interface SignalListener {
        void receives(String signal);
    }
}
