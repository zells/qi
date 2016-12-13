package org.zells.qi.model;

import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.model.deliver.Messenger;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.react.Reaction;
import org.zells.qi.model.refer.Name;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.*;

import java.util.*;

public class Cell {

    private Map<Name, Cell> children = new HashMap<>();
    private Cell parent;
    private Reaction reaction;
    private Path stem;
    private Set<Delivery> delivered = new HashSet<>();
    private Set<Peer> peers = new HashSet<>();

    public Cell() {
    }

    private Cell(Cell parent) {
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
        if (children.containsKey(Child.name(name))) {
            return children.get(Child.name(name));
        }
        return putChild(name, new Cell(this));
    }

    Cell putChild(String name, Cell child) {
        children.put(Child.name(name), child);
        return child;
    }

    public Cell join(Peer peer) {
        peers.add(peer);
        return this;
    }

    public Cell leave(Peer peer) {
        peers.remove(peer);
        return this;
    }

    public boolean deliver(Delivery delivery) {
        return !wasAlreadyDelivered(delivery)
                && (deliverToSelf(delivery)
                || deliverToParent(delivery)
                || deliverToParentRoot(delivery)
                || deliverToSelfRoot(delivery)
                || deliverToChild(delivery)
                || deliverToPeer(delivery)
                || deliverToStem(delivery));
    }

    private boolean wasAlreadyDelivered(Delivery delivery) {
        if (delivered.contains(delivery)) {
            return true;
        }
        delivered.add(delivery);
        return false;
    }

    private boolean deliverToSelf(Delivery delivery) {
        return delivery.hasArrived()
                && reaction != null
                && receive(delivery);
    }

    private boolean deliverToParent(Delivery delivery) {
        return !delivery.hasArrived()
                && delivery.nextName() instanceof Parent
                && parent != null
                && parent.deliver(delivery.toParent());
    }

    private boolean deliverToParentRoot(Delivery delivery) {
        return !delivery.hasArrived()
                && delivery.nextName() instanceof Root
                && parent != null
                && parent.deliver(delivery.toRoot());
    }

    private boolean deliverToSelfRoot(Delivery delivery) {
        return !delivery.hasArrived()
                && delivery.nextName() instanceof Root
                && deliver(delivery.toSelf());
    }

    private boolean deliverToChild(Delivery delivery) {
        return !delivery.hasArrived()
                && children.containsKey(delivery.nextName())
                && children.get(delivery.nextName()).deliver(delivery.toChild());
    }

    private boolean deliverToStem(Delivery delivery) {
        return stem != null
                && deliver(delivery.toStem(stem));
    }

    private boolean deliverToPeer(Delivery delivery) {
        for (Peer peer : peers) {
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
