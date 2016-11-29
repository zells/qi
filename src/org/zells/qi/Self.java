package org.zells.qi;

public class Self extends Name {

    private Self() {
    }

    @Override
    public String toString() {
        return ".";
    }

    static Name name() {
        return new Self();
    }
}
