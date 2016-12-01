package org.zells.qi;

class Delivery {
    private Path context;
    private Path receiver;
    private Path message;
    private Path role;

    Delivery(Path context, Path receiver, Path message) {
        this(context, receiver, message, context.with(receiver));
    }

    private Delivery(Path context, Path receiver, Path message, Path role) {
        this.context = context;
        this.receiver = receiver;
        this.message = message;
        this.role = role;
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
        return new Delivery(context, stem.with(receiver), message, role);
    }

    Delivery toStemExplicit(Path stem) {
        return new Delivery(context, stem.with(receiver.rest()), message);
    }

    Delivery toChild() {
        return new Delivery(context.with(nextName()), receiver.rest(), message.in(Parent.name()), role);
    }

    Delivery toParent() {
        return new Delivery(context.up(), receiver.rest(), message.in(context.last()), role);
    }

    Delivery toRoot() {
        return new Delivery(context.up(), receiver, message.in(context.last()), role);
    }

    Delivery toSelf() {
        return new Delivery(context, receiver.rest(), message, role);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Delivery
                && context.equals(((Delivery) obj).context)
                && receiver.equals(((Delivery)obj).receiver)
                && message.equals(((Delivery) obj).message);
    }

    @Override
    public int hashCode() {
        return context.hashCode() + receiver.hashCode() + message.hashCode() + role.hashCode();
    }

    @Override
    public String toString() {
        return role + "(" + receiver + " " + message + ")";
    }
}
