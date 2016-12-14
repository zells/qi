package org.zells.qi.model.refer.names;

import org.zells.qi.model.refer.Name;

public class Child extends Name {

    private String name;

    private Child(String name) {
        this.name = name;
    }

    public static Child name(String name) {
        return new Child(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
