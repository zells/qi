package org.zells.qi;

import java.util.HashMap;
import java.util.Map;

class Cell {

    private Map<Name, Cell> children = new HashMap<>();
    private Cell parent;
    private Reaction reaction;

    Cell() {
    }

    private Cell(Cell parent) {
        this.parent = parent;
    }

    boolean deliver(Delivery delivery) {
        if (delivery.hasArrived()) {
            if (reaction == null) {
                return false;
            }
            reaction.execute(this, delivery);
            return true;
        } else if (delivery.nextName().equals(Parent.name())) {
            return parent != null && parent.deliver(delivery.toParent());
        } else if (delivery.nextName().equals(Root.name())) {
            if (parent != null) {
                return parent.deliver(delivery.toRoot());
            } else {
                return deliver(delivery.toSelf());
            }
        } else if (children.containsKey(delivery.nextName())) {
            return children.get(delivery.nextName()).deliver(delivery.toChild());
        }

        return false;
    }

    Cell createChild(String name) {
        if (children.containsKey(Child.name(name))) {
            return children.get(Child.name(name));
        }
        return putChild(name, new Cell(this));
    }

    Cell putChild(String name, Cell child) {
        children.put(Child.name(name), child);
        return child;
    }

    void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }
}
