package org.zells.qi.refer.names;

import org.zells.qi.refer.Name;

public class Stem extends Name {

    private Stem() {
    }

    public static Name name() {
        return new Stem();
    }

    @Override
    public String toString() {
        return "*";
    }
}
