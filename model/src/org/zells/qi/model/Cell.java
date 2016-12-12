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
        if (delivered.contains(delivery)) {
            return false;
        }
        delivered.add(delivery);

        if (delivery.hasArrived()) {
            if (reaction == null) {
                for (Peer peer : peers) {
                    boolean delivered = peer.deliver(delivery);
                    if (delivered) {
                        return true;
                    }
                }

                return stem != null && deliver(delivery.toStem(stem));
            }

            return receive(delivery);
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
