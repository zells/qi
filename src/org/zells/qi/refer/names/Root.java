package org.zells.qi.refer.names;

import org.zells.qi.refer.Name;

public class Root extends Name {

    private Root() {
    }

    public static Name name() {
        return new Root();
    }

    @Override
    public String toString() {
        return "°";
    }
}
