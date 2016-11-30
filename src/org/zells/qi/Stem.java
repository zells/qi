package org.zells.qi;

public class Stem extends Name {

    private Stem() {
    }

    static Name name() {
        return new Stem();
    }

    @Override
    public String toString() {
        return "*";
    }
}
