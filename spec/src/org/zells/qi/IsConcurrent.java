package org.zells.qi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.zells.qi.deliver.Messenger;
import org.zells.qi.react.DynamicReaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class IsConcurrent extends Specification {

    @Test
    void FailedDelivery() {
        Cell cell = new Cell();

        final boolean[] failed = {false};
        Messenger messenger =
                (new Messenger(cell, delivery("r", "foo", "m")))
                        .setMaxRetries(0)
                        .whenFailed(() -> failed[0] = true)
                        .run()
                        .waitForIt();

        assertFalse(messenger.hasDelivered());
        assertTrue(failed[0]);
    }


    @Test
    void SuccessfulDelivery() {
        Cell cell = new Cell();
        cell.createChild("foo").setReaction(catchDelivery());

        Messenger messenger =
                (new Messenger(cell, delivery("r", "foo", "m")))
                        .setMaxRetries(0)
                        .run()
                        .waitForIt();

        assertTrue(messenger.hasDelivered());
        assertEquals("r.foo( ^.m)", delivered.toString());
    }


    @Test
    void RepeatDelivery() throws InterruptedException {
        Cell cell = new Cell();
        Messenger messenger =
                (new Messenger(cell, delivery("r", "foo", "m")))
                        .setMaxRetries(5)
                        .run();

        Thread.sleep(15);
        cell.createChild("foo").setReaction(catchDelivery());

        messenger.waitForIt();

        assertTrue(messenger.hasDelivered());
        assertEquals("r.foo( ^.m)", delivered.toString());
    }

    @Test
    void RepeatMessageSends() throws InterruptedException {
        Cell cell = new Cell();
        cell.setReaction((new DynamicReaction())
                .add(send("foo", "m"))
                .add(send("bar", "m")));

        deliver(cell, "r", "", "");

        Thread.sleep(5);
        cell.putChild("bar", (new Cell()).setReaction(catchDelivery()));
        waitForDeliveryTo("r.bar( ^.m)");

        Thread.sleep(5);
        cell.putChild("foo", (new Cell()).setReaction(catchDelivery()));
        waitForDeliveryTo("r.foo( ^.m)");
    }

    private void waitForDeliveryTo(String to) throws InterruptedException {
        while (delivered == null) {
            Thread.sleep(5);
        }
        assertEquals(to, delivered.toString());
        delivered = null;
    }
}
