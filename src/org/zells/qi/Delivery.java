package org.zells.qi;

class Delivery {
    private Path context;
    private Path receiver;
    private Path message;

    Delivery(Path context, Path receiver, Path message) {
        this.context = context;
        this.receiver = receiver;
        this.message = message;
    }

    boolean hasArrived() {
        return receiver.isEmpty();
    }

    Name nextName() {
        return receiver.first();
    }

    Delivery toChild() {
        return new Delivery(context.with(nextName()), receiver.rest(), message.in(Parent.name()));
    }

    Delivery toParent() {
        return new Delivery(context.up(), receiver.rest(), message.in(context.last()));
    }

    Delivery toRoot() {
        return new Delivery(context.up(), receiver, message.in(context.last()));
    }

    Delivery toSelf() {
        return new Delivery(context, receiver.rest(), message);
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
        return context.hashCode() + receiver.hashCode() + message.hashCode();
    }

    @Override
    public String toString() {
        return context + "(" + receiver + " " + message + ")";
    }
}
