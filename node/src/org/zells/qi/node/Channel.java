package org.zells.qi.node;

public interface Channel {

    Signal send(String signal);

    void listen(SignalListener listener);

    interface SignalListener {
        void receives(String signal);
    }
}
