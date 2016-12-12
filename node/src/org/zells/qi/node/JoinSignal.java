package org.zells.qi.node;

import org.zells.qi.model.refer.Path;

class JoinSignal implements Signal {
    private Path path;
    private String connection;

    JoinSignal(Path path, String connection) {
        this.path = path;
        this.connection = connection;
    }

    String getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "JOIN " + path + " " + connection;
    }
}
