package org.zells.qi.node.singalling.signals;

import org.zells.qi.model.refer.Path;
import org.zells.qi.node.singalling.Signal;

public class JoinSignal implements Signal {

    private Path path;
    private String connection;

    public JoinSignal(Path path, String connection) {
        this.path = path;
        this.connection = connection;
    }

    public Path getPath() {
        return path;
    }

    public String getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "JOIN " + path + " " + connection;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JoinSignal && toString().equals(obj.toString());
    }
}
