package org.zells.qi.node;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.Reaction;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.fakes.FakeChannel;
import org.zells.qi.node.fakes.FakeNode;
import org.zells.qi.node.singalling.signals.DeliverSignal;
import org.zells.qi.node.singalling.signals.FailedSignal;
import org.zells.qi.node.singalling.signals.ReceivedSignal;

import static org.junit.jupiter.api.Assertions.*;

public class DeliversMessagesTest {

    @BeforeEach
    void SetUp() {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            private int i = 0;

            @Override
            protected String next() {
                return "fizz-baz-" + i++;
            }
        });
        FakeChannel.reset();
        node = new FakeNode("fake", (new Cell()).createChild("foo"), new Path(Root.name(), Child.name("foo")));
    }

    @Test
    void DeliverLocally() {
        Cell bar = node.cell.createChild("bar");
        bar.setReaction(catchMessage());

        node.send(new Path(Child.name("bar")), new Path(Child.name("baz")));

        waitForReceived();
        assertEquals(new Path(Root.name(), Child.name("foo"), Child.name("baz")), received);
    }

    @Test
    void DeliveryFails() {
        node.send(new Path(Child.name("bar")), new Path());

        assertNull(received);
    }

    @Test
    void RetryDelivery() {
        node.send(new Path(Child.name("bar")), new Path(Child.name("baz")));

        Cell bar = node.cell.createChild("bar");
        bar.setReaction(catchMessage());

        waitForReceived();
        assertEquals(new Path(Root.name(), Child.name("foo"), Child.name("baz")), received);
    }

    @Test
    void IncomingDelivery() {
        Cell bar = node.cell.createChild("bar");
        bar.setReaction(catchMessage());

        node.server.receive(new DeliverSignal(
                new Path(Root.name(), Child.name("foo")),
                new Path(Child.name("bar")),
                new Path(Root.name(), Child.name("foo"), Child.name("bar")),
                new Path(Root.name(), Child.name("baz")),
                "global-unique-id"
        ));

        assertEquals(new Path(Root.name(), Child.name("baz")), received);
        assertTrue(node.server.responded instanceof ReceivedSignal);
    }

    @Test
    void IncomingDeliveryFails() {
        node.server.receive(new DeliverSignal(
                new Path(Root.name(), Child.name("foo")),
                new Path(Child.name("bar")),
                new Path(Root.name(), Child.name("foo"), Child.name("bar")),
                new Path(Root.name(), Child.name("baz")),
                "global-unique-id"
        ));

        assertEquals(null, received);
        assertTrue(node.server.responded instanceof FailedSignal);
    }

    private Path received;
    private FakeNode node;

    private Reaction catchMessage() {
        return delivery -> received = delivery.getMessage();
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
