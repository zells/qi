package org.zells.qi.node.signals;

import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Signal;

public class JoinSignal implements Signal {
    private Path path;
    private String connection;

    public JoinSignal(Path path, String connection) {
        this.path = path;
        this.connection = connection;
    }

    public String getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "JOIN " + path + " " + connection;
    }
}
