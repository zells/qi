package org.zells.qi.model.refer.names;

import org.zells.qi.model.refer.Name;

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
