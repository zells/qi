package org.zells.qi.model.refer.names;

import org.zells.qi.model.refer.Name;

public class Root extends Name {

    private Root() {
    }

    public static Name name() {
        return new Root();
    }

    @Override
    public String toString() {
        return "*";
    }
}
