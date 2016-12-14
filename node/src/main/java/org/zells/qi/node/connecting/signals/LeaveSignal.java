package org.zells.qi.node.connecting.signals;

import org.zells.qi.model.refer.Path;
import org.zells.qi.node.connecting.Signal;

public class LeaveSignal implements Signal {

    private final Path path;
    private final String connection;

    public LeaveSignal(Path path, String connection) {
        this.path = path;
        this.connection = connection;
    }

    public String getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "LEAVE " + path + " " + connection;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LeaveSignal && toString().equals(obj.toString());
    }
}
