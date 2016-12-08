package org.zells.qi.deliver;

import org.zells.qi.refer.Name;
import org.zells.qi.refer.Path;
import org.zells.qi.refer.names.Parent;

public class Delivery {
    private Path context;
    private Path receiver;
    private Path message;
    private Path target;
    private String guid;

    public Delivery(Path context, Path receiver, Path message) {
        this(context, receiver, receiver, message, GlobalUniqueIdentifierGenerator.generate());
    }

    private Delivery(Path context, Path target, Path receiver, Path message, String guid) {
        this.context = context;
        this.receiver = receiver;
        this.message = message;
        this.target = target;
        this.guid = guid;
    }

    Delivery renew() {
        return new Delivery(context, target, receiver, message, GlobalUniqueIdentifierGenerator.generate());
    }

    public Path getMessage() {
        return message;
    }

    public boolean hasArrived() {
        return target.isEmpty();
    }

    public Name nextName() {
        return target.first();
    }

    public Delivery send(Path to, Path message) {
        return new Delivery(context, receiver.with(to), receiver.with(message));
    }

    public Delivery toStem(Path stem) {
        return new Delivery(context, stem.with(target), receiver, message, guid);
    }

    public Delivery toChild() {
        return new Delivery(context.with(target.first()), target.rest(), receiver, message, guid);
    }

    public Delivery toParent() {
        return new Delivery(context.up(), target.rest(), receiver, message, guid);
    }

    public Delivery toRoot() {
        return new Delivery(context.up(), target, receiver, message, guid);
    }

    public Delivery toSelf() {
        return new Delivery(context, target.rest(), receiver, message, guid);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Delivery
                && context.equals(((Delivery) obj).context)
                && target.equals(((Delivery)obj).target)
                && receiver.equals(((Delivery) obj).receiver)
                && message.equals(((Delivery) obj).message)
                && guid.equals(((Delivery) obj).guid);
    }

    @Override
    public int hashCode() {
        return context.hashCode() + target.hashCode() + receiver.hashCode() + message.hashCode() + guid.hashCode();
    }

    @Override
    public String toString() {
        return context + "|" + target + "(" + receiver + " " + message + ")";
    }
}
