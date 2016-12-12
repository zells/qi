package org.zells.qi.node;

import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;

import java.util.HashSet;
import java.util.Set;

public abstract class Node {

    private Set<MessageListener> listeners = new HashSet<>();

    public abstract void send(MessageSend messageSend);

    public void waitForMessage(MessageListener listener) {
        listeners.add(listener);
    }

    protected void receive(Path message) {
        for (MessageListener listener : listeners) {
            listener.receives(message);
        }
    }

    public interface MessageListener {
        void receives(Path message);
    }
}
