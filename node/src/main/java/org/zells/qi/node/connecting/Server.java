package org.zells.qi.node.connecting;

import org.zells.qi.node.singalling.Signal;

public interface Server {

    void start(SignalListener listener);

    void stop();

    String getConnection();

    interface SignalListener {
        Signal receives(Signal signal);
    }
}
