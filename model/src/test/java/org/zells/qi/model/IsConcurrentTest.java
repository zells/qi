package org.zells.qi.model;

import org.junit.jupiter.api.Test;
import org.zells.qi.model.deliver.Messenger;

import static org.junit.jupiter.api.Assertions.*;

public class IsConcurrentTest extends Specification {

    @Test
    void FailedDelivery() {
        Cell cell = new Cell();

        final boolean[] failed = {false};
        Messenger messenger =
                (new Messenger(cell, delivery("", "foo", "m")))
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
        cell.createChild("foo").setReaction(catchMessage());

        Messenger messenger =
                (new Messenger(cell, delivery("*", "foo", "m")))
                        .setMaxRetries(0)
                        .run()
                        .waitForIt();

        assertTrue(messenger.hasDelivered());
        assertEquals("*.m", received.toString());
    }


    @Test
    void RepeatDelivery() throws InterruptedException {
        Cell cell = new Cell();
        Messenger messenger =
                (new Messenger(cell, delivery("*", "foo", "m")))
                        .setMaxRetries(5)
                        .run();

        Thread.sleep(15);
        cell.createChild("foo").setReaction(catchMessage());

        messenger.waitForIt();

        assertTrue(messenger.hasDelivered());
        assertEquals("*.m", received.toString());
    }
}
