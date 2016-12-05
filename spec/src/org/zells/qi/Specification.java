package org.zells.qi;

import org.junit.jupiter.api.BeforeEach;
import org.zells.qi.deliver.Delivery;
import org.zells.qi.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.react.MessageSend;
import org.zells.qi.react.Reaction;
import org.zells.qi.refer.Name;
import org.zells.qi.refer.Path;
import org.zells.qi.refer.names.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Specification {

    boolean wasDelivered;
    Delivery delivered;

    @BeforeEach
    void SetUp() {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            private int id = 0;
            protected String next() {
                return (new Integer(id++)).toString();
            }
        });
    }

    void assertWasDelivered(String to) {
        while (delivered == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        }
        assertTrue(wasDelivered);
        assertEquals(to, delivered.toString());
    }

    Reaction catchDelivery() {
        return (delivery) -> {
            delivered = delivery;
            return new ArrayList<>();
        };
    }

    void deliver(Cell sending, String context, String receiver, String message) {
        wasDelivered = sending.deliver(delivery(context, receiver, message));
    }

    MessageSend send(String receiver, String message) {
        return new MessageSend(path(receiver), path(message));
    }

    Delivery delivery(String context, String receiver, String message) {
        return new Delivery(path(context), path(receiver), path(message));
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
