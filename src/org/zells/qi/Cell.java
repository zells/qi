package org.zells.qi;

import org.zells.qi.deliver.Delivery;
import org.zells.qi.react.MessageSend;
import org.zells.qi.react.Reaction;
import org.zells.qi.refer.Name;
import org.zells.qi.refer.Path;
import org.zells.qi.refer.names.Child;
import org.zells.qi.refer.names.Parent;
import org.zells.qi.refer.names.Root;
import org.zells.qi.refer.names.Stem;

import java.util.*;

class Cell {

    private Map<Name, Cell> children = new HashMap<>();
    private Cell parent;
    private Reaction reaction;
    private Path stem;
    private Set<Delivery> delivered = new HashSet<>();

    Cell() {
    }

    private Cell(Cell parent) {
        this.parent = parent;
    }

    Cell setStem(Path stem) {
        this.stem = stem;
        return this;
    }

    Cell setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }

    Cell createChild(String name) {
        if (children.containsKey(Child.name(name))) {
            return children.get(Child.name(name));
        }
        return putChild(name, new Cell(this));
    }

    private Cell putChild(String name, Cell child) {
        children.put(Child.name(name), child);
        return child;
    }

    boolean deliver(Delivery delivery) {
        if (delivered.contains(delivery)) {
            return false;
        }
        delivered.add(delivery);

        if (delivery.hasArrived()) {
            if (reaction == null) {
                return stem != null && deliver(delivery.toStem(stem));
            }

            return executeReaction(delivery);
        } else if (delivery.nextName().equals(Stem.name())) {
            return stem != null && deliver(delivery.toStemExplicit(stem));
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
        } else if (stem != null) {
            return deliver(delivery.toStem(stem));
        }

        return false;
    }

    private boolean executeReaction(Delivery delivery) {
        List<MessageSend> sends = reaction.execute(delivery);
        for (MessageSend s : sends) {
            deliver(delivery.send(s.getReceiver(), s.getMessage()));
        }
        return true;
    }
}
