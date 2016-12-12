package org.zells.qi.node;

import org.zells.qi.model.refer.Path;

class LeaveSignal implements Signal {
    private final Path path;
    private final String connection;

    LeaveSignal(Path path, String connection) {
        this.path = path;
        this.connection = connection;
    }

    String getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "LEAVE " + path + " " + connection;
    }
}
