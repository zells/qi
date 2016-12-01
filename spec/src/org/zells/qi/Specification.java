package org.zells.qi;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Specification {

    boolean wasDelivered;
    Delivery delivered;

    @BeforeEach
    void SetUp() {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            private int id = 0;
            String next() {
                return (new Integer(id++)).toString();
            }
        });
    }

    void assertWasDelivered(String to) {
        assertTrue(wasDelivered);
        assertEquals(to, delivered.toString());
    }

    Reaction catchDelivery() {
        return (Cell cell, Delivery delivery) -> delivered = delivery;
    }

    void deliver(Cell sending, String context, String receiver, String message) {
        wasDelivered = sending.deliver(new Delivery(p(context), p(receiver), p(message)));
    }

    MessageSend send(String receiver, String message) {
        return new MessageSend(p(receiver), p(message));
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
            case "*":
                return Stem.name();
            default:
                return Child.name(string);
        }
    }
}
