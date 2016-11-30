package org.zells.qi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsDynamic {

    @Test
    @Disabled
    void NoReaction() {
        Cell cell = new Cell();
        assertFalse(cell.deliver(new Delivery(new Path(), new Path(), new Path())));
    }

    @Test
    @Disabled
    void ExecuteReaction() {
        final boolean[] executed = new boolean[1];

        Cell cell = new Cell();
        cell.setReaction(() -> executed[0] = true);

        assertTrue(cell.deliver(new Delivery(new Path(), new Path(), new Path())));
        assertTrue(executed[0]);
    }

    @Test
    @Disabled("TBD")
    void NoMessageSends() {
    }

    @Test
    @Disabled("TBD")
    void ExecuteMessageSends() {
    }

    @Test
    @Disabled("TBD")
    void SendMessage() {
    }

    @Test
    @Disabled("TBD")
    void SendToMessage() {
    }

    @Test
    @Disabled("TBD")
    void SendFrame() {
    }

    @Test
    @Disabled("TBD")
    void SendToFrame() {
    }
}
