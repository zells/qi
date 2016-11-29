package org.zells.qi;

public class Child extends Name {

    private String name;

    private Child(String name) {
        this.name = name;
    }

    static Child name(String name) {
        return new Child(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
