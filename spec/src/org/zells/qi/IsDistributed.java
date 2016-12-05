package org.zells.qi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.zells.qi.deliver.Delivery;

import static org.junit.jupiter.api.Assertions.*;

public class IsDistributed extends Specification {

    @Test
    void NoPeers() {
        Cell cell = new Cell();
        deliver(cell, "r", "foo", "m");
        assertFalse(wasDelivered);
    }

    @Test
    void DeliverToPeer() {
        Cell cell = new Cell();
        cell.join(delivery -> {
            delivered = delivery;
            return true;
        });

        deliver(cell, "r", "", "m");
        assertWasDelivered("r( m)");
    }

    @Test
    void FailPeer() {
        Cell cell = new Cell();
        cell.join(delivery -> {
            delivered = delivery;
            return false;
        });

        deliver(cell, "r", "", "m");
        assertNotNull(delivered);
        assertFalse(wasDelivered);
    }

    @Test
    void DeliverToAllPeers() {
        boolean[] reached = {false, false};

        Cell cell = new Cell();
        cell.join(delivery -> {
            reached[0] = true;
            return false;
        });
        cell.join(delivery -> {
            reached[1] = true;
            return false;
        });

        deliver(cell, "r", "", "m");

        assertTrue(reached[0]);
        assertTrue(reached[1]);
    }

    @Test
    void LeavePeer() {
        Cell cell = new Cell();
        Peer peer = delivery -> true;

        cell.join(peer);
        cell.join(delivery -> false);

        deliver(cell, "r", "", "m");
        assertTrue(wasDelivered);

        cell.leave(peer);

        deliver(cell, "r", "", "m");
        assertFalse(wasDelivered);
    }

    @Test
    void PeerBeforeStem() {
        final boolean[] deliveredToPeer = {false};

        Cell cell = new Cell();
        cell.createChild("foo")
                .setStem(path("^.bar"))
                .join(delivery -> {
                    deliveredToPeer[0] = true;
                    return true;
                });
        cell.createChild("bar")
                .setReaction(catchDelivery());

        deliver(cell, "r", "foo", "m");
        assertTrue(deliveredToPeer[0]);
    }
}
