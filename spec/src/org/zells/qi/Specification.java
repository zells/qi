package org.zells.qi;

import org.junit.jupiter.api.BeforeEach;
import org.zells.qi.deliver.Delivery;
import org.zells.qi.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.react.MessageSend;
import org.zells.qi.react.Reaction;
import org.zells.qi.refer.Name;
import org.zells.qi.refer.Path;
import org.zells.qi.refer.names.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class Specification {

    boolean wasDelivered;
    Path received;

    @BeforeEach
    void SetUp() {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            private int id = 0;
            protected String next() {
                return (new Integer(id++)).toString();
            }
        });
    }

    void assertWasReceived(String to) {
        int count = 0;
        while (received == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
            if (count++ > 10) {
                fail("Was not received");
            }
        }
        assertTrue(wasDelivered);
        assertEquals(to, received.toString());
    }

    Reaction catchMessage() {
        return (message) -> {
            received = message;
            return null;
        };
    }

    void deliver(Cell sending, String context, String receiver, String message) {
        wasDelivered = sending.deliver(delivery(context, receiver, message));
    }

    MessageSend send(String receiver, String message) {
        return new MessageSend(path(receiver), path(message));
    }

    Delivery delivery(String context, String receiver, String message) {
        return new Delivery(path(context), path(context).with(path(receiver)), path(context).with(path(message)));
    }

    Path path(String string) {
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
            case "*":
                return Stem.name();
            default:
                return Child.name(string);
        }
    }
}
