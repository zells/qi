package org.zells.qi;

public class Root extends Name {

    private Root() {
    }

    static Name name() {
        return new Root();
    }

    @Override
    public String toString() {
        return "°";
    }
}
