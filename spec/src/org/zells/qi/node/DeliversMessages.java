package org.zells.qi.node;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.react.Reaction;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.fakes.FakeNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeliversMessages {

    @BeforeEach
    void SetUp() {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            private int i = 0;

            @Override
            protected String next() {
                return "fizz-baz-" + i++;
            }
        });
        node = new FakeNode(new Path(Root.name()));
    }

    @Test
    void DeliverLocally() {
        Cell bar = node.cell.createChild("bar");
        bar.setReaction(catchMessage());

        node.send(new MessageSend(new Path(Child.name("bar")), new Path(Child.name("baz"))));

        waitForReceived();
        assertEquals(new Path(Root.name(), Child.name("baz")), received);
    }

    @Test
    void DeliveryFails() {
        node.send(new MessageSend(new Path(Child.name("bar")), new Path()));

        waitForReceived();
        assertNull(received);
    }

    @Test
    void RetryDelivery() {
        node.send(new MessageSend(new Path(Child.name("bar")), new Path(Child.name("baz"))));

        Cell bar = node.cell.createChild("bar");
        bar.setReaction(catchMessage());

        waitForReceived();
        assertEquals(new Path(Root.name(), Child.name("baz")), received);
    }

    @Test
    void IncomingDelivery() {
        Cell bar = node.cell.createChild("bar");
        bar.setReaction(catchMessage());

        node.channel.receive("DELIVER °.foo bar °.foo.bar °.baz global-unique-id");

        waitForReceived();
        assertEquals(new Path(Root.name(), Child.name("baz")), received);
        assertEquals("RECEIVED", node.channel.sent);
    }

    @Test
    void IncomingDeliveryFails() {
        node.channel.receive("DELIVER °.foo bar °.foo.bar °.baz global-unique-id");

        waitForReceived();
        assertEquals(null, received);
        assertEquals("FAILED", node.channel.sent);
    }

    private Path received;
    private FakeNode node;

    private Reaction catchMessage() {
        return message -> {
            received = message;
            return null;
        };
    }

    private void waitForReceived() {
        for (int i = 0; i < 20 && received == null; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
