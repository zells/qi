package org.zells.qi.node.fakes;

import org.zells.qi.node.connecting.Channel;
import org.zells.qi.node.connecting.Signal;
import org.zells.qi.node.connecting.signals.OkSignal;

import java.util.HashSet;
import java.util.Set;

public class FakeChannel implements Channel {

    public String sent;
    private Signal response = new OkSignal();
    private Set<Channel.SignalListener> listeners = new HashSet<>();

    public void receive(String signal) {
        for (Channel.SignalListener listener : listeners) {
            listener.receives(signal);
        }
    }

    @Override
    public Signal send(String signal) {
        sent = signal;
        return response;
    }

    @Override
    public void listen(SignalListener listener) {
        listeners.add(listener);
    }
}
