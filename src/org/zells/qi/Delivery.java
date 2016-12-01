package org.zells.qi;

class Delivery {
    private Path context;
    private Path receiver;
    private Path message;
    private Path role;
    private String guid;

    Delivery(Path context, Path receiver, Path message) {
        this(context, receiver, message, context.with(receiver), GlobalUniqueIdentifierGenerator.generate());
    }

    private Delivery(Path context, Path receiver, Path message, Path role, String guid) {
        this.context = context;
        this.receiver = receiver;
        this.message = message;
        this.role = role;
        this.guid = guid;
    }

    Path getMessage() {
        return message;
    }

    boolean hasArrived() {
        return receiver.isEmpty();
    }

    Name nextName() {
        return receiver.first();
    }

    Delivery send(Path receiver, Path message) {
        return new Delivery(context, receiver, message);
    }

    Delivery toStem(Path stem) {
        return new Delivery(context, stem.with(receiver), message, role, guid);
    }

    Delivery toStemExplicit(Path stem) {
        return new Delivery(context, stem.with(receiver.rest()), message);
    }

    Delivery toChild() {
        return new Delivery(context.with(nextName()), receiver.rest(), message.in(Parent.name()), role, guid);
    }

    Delivery toParent() {
        return new Delivery(context.up(), receiver.rest(), message.in(context.last()), role, guid);
    }

    Delivery toRoot() {
        return new Delivery(context.up(), receiver, message.in(context.last()), role, guid);
    }

    Delivery toSelf() {
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
