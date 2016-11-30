package org.zells.qi;

@SuppressWarnings("SameParameterValue")
class Specification {

    boolean wasDelivered;

    void deliver(Cell sending, String context, String receiver, String message) {
        wasDelivered = sending.deliver(new Delivery(p(context), p(receiver), p(message)));
    }

    Path p(String string) {
        Path path = new Path();
        if (string.isEmpty()) {
            return path;
        }

        for (String name : string.split("\\.")) {
            path = path.with(nameOf(name));
        }
        return path;
    }

    private Name nameOf(String string) {
        switch (string) {
            case "^":
                return Parent.name();
            case "°":
                return Root.name();
            case "@":
                return Message.name();
            case "#":
                return Frame.name();
            default:
                return Child.name(string);
        }
    }
}
