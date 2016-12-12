package org.zells.qi.node.signals;

import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Signal;

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
}
