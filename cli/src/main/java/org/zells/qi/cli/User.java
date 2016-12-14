package org.zells.qi.cli;

import java.util.HashSet;
import java.util.Set;

public abstract class User {

    private Set<InputListener> listeners = new HashSet<>();

    public abstract void tell(String output);

    void listen(InputListener listener) {
        listeners.add(listener);
    }

    protected void hear(String input) {
        for (InputListener listener : listeners) {
            listener.hears(input);
        }
    }

    interface InputListener {
        void hears(String input);
    }
}
