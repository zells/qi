package org.zells.qi.refer.names;

import org.zells.qi.refer.Name;

public class Parent extends Name {

    private Parent() {
    }

    public static Name name() {
        return new Parent();
    }

    @Override
    public String toString() {
        return "^";
    }
}
