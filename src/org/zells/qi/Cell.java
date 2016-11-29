package org.zells.qi;

import java.util.HashMap;
import java.util.Map;

class Cell {

    private Map<Name, Cell> children = new HashMap<>();
    private Cell parent;

    Cell() {
    }

    private Cell(Cell parent) {
        this.parent = parent;
    }

    boolean deliver(Path receiver, Path message) {
        if (receiver.isEmpty()) {
            return true;
        } else if (receiver.first().equals(Self.name())) {
            return deliver(receiver.rest(), message);
        } else if (receiver.first().equals(Parent.name())) {
            return parent != null && parent.deliver(receiver.rest(), message);
        } else if (receiver.first().equals(Root.name())) {
            if (parent != null) {
                return parent.deliver(receiver, message);
            } else {
                return deliver(receiver.rest(), message);
            }
        } else if (children.containsKey(receiver.first())) {
            return children.get(receiver.first()).deliver(receiver.rest(), message);
        }

        return false;
    }

    Cell createChild(String name) {
        return putChild(name, new Cell(this));
    }

    private Cell putChild(String name, Cell child) {
        children.put(Child.name(name), child);
        return child;
    }
}
