package org.zells.qi.deliver;

import org.zells.qi.refer.Name;
import org.zells.qi.refer.Path;
import org.zells.qi.refer.names.Parent;

public class Delivery {
    private Path context;
    private Path receiver;
    private Path message;
    private Path role;
    private String guid;

    public Delivery(Path context, Path receiver, Path message) {
        this(context, receiver, message, context.with(receiver), GlobalUniqueIdentifierGenerator.generate());
    }

    private Delivery(Path context, Path receiver, Path message, Path role, String guid) {
        this.context = context;
        this.receiver = receiver;
        this.message = message;
        this.role = role;
        this.guid = guid;
    }

    public Path getMessage() {
        return message;
    }

    public boolean hasArrived() {
        return receiver.isEmpty();
    }

    public Name nextName() {
        return receiver.first();
    }

    public Delivery send(Path receiver, Path message) {
        return new Delivery(context, receiver, message);
    }

    public Delivery toStem(Path stem) {
        return new Delivery(context, stem.with(receiver), message, role, guid);
    }

    public Delivery toStemExplicit(Path stem) {
        return new Delivery(context, stem.with(receiver.rest()), message);
    }

    public Delivery toChild() {
        return new Delivery(context.with(nextName()), receiver.rest(), message.in(Parent.name()), role, guid);
    }

    public Delivery toParent() {
        return new Delivery(context.up(), receiver.rest(), message.in(context.last()), role, guid);
    }

    public Delivery toRoot() {
        return new Delivery(context.up(), receiver, message.in(context.last()), role, guid);
    }

    public Delivery toSelf() {
        return new Delivery(context, receiver.rest(), message, role, guid);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Delivery
                && context.equals(((Delivery) obj).context)
                && receiver.equals(((Delivery)obj).receiver)
                && message.equals(((Delivery) obj).message)
                && role.equals(((Delivery) obj).role)
                && guid.equals(((Delivery) obj).guid);
    }

    @Override
    public int hashCode() {
        return context.hashCode() + receiver.hashCode() + message.hashCode() + role.hashCode() + guid.hashCode();
    }

    @Override
    public String toString() {
        return role + "(" + receiver + " " + message + ")";
    }
}
