package org.zells.qi.model;

import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.model.deliver.Messenger;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.react.Reaction;
import org.zells.qi.model.refer.Name;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.*;

import java.util.*;

public class Cell implements Courier {

    private Map<Name, Courier> children = new HashMap<>();
    private Courier parent;
    private Reaction reaction;
    private Path stem;
    private Set<Delivery> delivered = new HashSet<>();
    private Set<Courier> peers = new HashSet<>();

    public Cell() {
    }

    private Cell(Courier parent) {
        this.parent = parent;
    }

    Cell setStem(Path stem) {
        this.stem = stem;
        return this;
    }

    public Cell setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }

    public Cell createChild(String name) {
        Cell child = new Cell(this);
        putChild(name, child);
        return child;
    }

    public Cell putChild(String name, Courier child) {
        children.put(Child.name(name), child);
        return this;
    }

    public Cell join(Courier peer) {
        peers.add(peer);
        return this;
    }

    public Cell leave(Courier peer) {
        peers.remove(peer);
        return this;
    }

    public boolean deliver(Delivery delivery) {
        if (wasAlreadyDelivered(delivery)) {
            return false;
        }

        if (delivery.hasArrived()) {
            return deliverToSelf(delivery)
                    || deliverToPeer(delivery)
                    || deliverToStem(delivery);
        }

        if (delivery.nextName() instanceof Parent) {
            return deliverToParent(delivery);
        } else if (delivery.nextName() instanceof Root) {
            return deliverToSelfRoot(delivery)
                    || deliverToParentRoot(delivery);
        }

        return deliverToChild(delivery)
                || deliverToPeer(delivery)
                || deliverToStem(delivery);
    }

    private boolean wasAlreadyDelivered(Delivery delivery) {
        if (delivered.contains(delivery)) {
            return true;
        }
        delivered.add(delivery);
        return false;
    }

    private boolean deliverToSelf(Delivery delivery) {
        return reaction != null
                && receive(delivery);
    }

    private boolean deliverToParent(Delivery delivery) {
        return parent != null
                && parent.deliver(delivery.toParent());
    }

    private boolean deliverToParentRoot(Delivery delivery) {
        return parent != null
                && parent.deliver(delivery.toRoot());
    }

    private boolean deliverToSelfRoot(Delivery delivery) {
        return parent == null
                && deliver(delivery.toSelf());
    }

    private boolean deliverToChild(Delivery delivery) {
        return children.containsKey(delivery.nextName())
                && children.get(delivery.nextName()).deliver(delivery.toChild());
    }

    private boolean deliverToStem(Delivery delivery) {
        return stem != null
                && deliver(delivery.toStem(stem));
    }

    private boolean deliverToPeer(Delivery delivery) {
        for (Courier peer : peers) {
            if (peer.deliver(delivery)) {
                return true;
            }
        }
        return false;
    }

    private boolean receive(Delivery delivery) {
        List<MessageSend> sends = reaction.execute(delivery.getMessage());

        if (sends == null) {
            return true;
        }

        for (MessageSend send : sends) {
            Delivery next = delivery.send(
                    resolve(send.getReceiver(), delivery.getMessage()),
                    resolve(send.getMessage(), delivery.getMessage())
            );
            (new Messenger(this, next)).run();
        }
        return true;
    }

    private Path resolve(Path path, Path message) {
        if (path.isEmpty()) {
            return path;
        } else if (path.first().equals(Message.name())) {
            return message.with(path.rest());
        } else if (path.first().equals(Stem.name())) {
            return stem.with(path.rest());
        } else {
            return path;
        }
    }
}
