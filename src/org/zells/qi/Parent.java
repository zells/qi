package org.zells.qi;

public class Parent extends Name {

    private Parent() {
    }

    @Override
    public String toString() {
        return "^";
    }

    static Name name() {
        return new Parent();
    }
}
