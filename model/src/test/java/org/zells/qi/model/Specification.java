package org.zells.qi.model;

import org.junit.jupiter.api.BeforeEach;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.refer.Name;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.*;

import static org.junit.jupiter.api.Assertions.*;

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
        return (delivery) -> received = delivery.getMessage();
    }

    void deliver(Cell sending, String context, String receiver, String message) {
        wasDelivered = sending.deliver(delivery(context, receiver, message));
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
            case "*":
                return Root.name();
            default:
                return Child.name(string);
        }
    }
}
