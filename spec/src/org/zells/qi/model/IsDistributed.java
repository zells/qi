package org.zells.qi.model;

import org.junit.jupiter.api.Test;
import org.zells.qi.model.refer.Path;

import static org.junit.jupiter.api.Assertions.*;

public class IsDistributed extends Specification {

    private Path target;

    @Test
    void NoPeers() {
        Cell cell = new Cell();
        deliver(cell, "", "", "");
        assertFalse(wasDelivered);
    }

    @Test
    void DeliverSelfToPeer() {
        Cell cell = new Cell();
        cell.join(delivery -> {
            received = delivery.getMessage();
            return true;
        });

        deliver(cell, "°", "", "m");
        assertWasReceived("°.m");
    }

    @Test
    void DeliverChildToPeer() {
        Cell cell = new Cell();
        cell.join(delivery -> {
            received = delivery.getMessage();
            target = delivery.getTarget();
            return true;
        });

        deliver(cell, "°", "foo", "m");

        assertWasReceived("°.m");
        assertEquals(path("foo"), target);
    }

    @Test
    void DeliverGrandchildToPeer() {
        Cell cell = new Cell();
        cell.join(delivery -> {
            received = delivery.getMessage();
            target = delivery.getTarget();
            return true;
        });

        deliver(cell, "°", "foo.bar", "m");

        assertWasReceived("°.m");
        assertEquals(path("foo.bar"), target);
    }

    @Test
    void FailPeer() {
        Cell cell = new Cell();
        cell.join(delivery -> {
            received = delivery.getMessage();
            return false;
        });

        deliver(cell, "°", "", "m");
        assertNotNull(received);
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

        deliver(cell, "°", "", "m");

        assertTrue(reached[0]);
        assertTrue(reached[1]);
    }

    @Test
    void LeavePeer() {
        Cell cell = new Cell();
        Peer peer = delivery -> true;

        cell.join(peer);
        cell.join(delivery -> false);

        deliver(cell, "°", "", "m");
        assertTrue(wasDelivered);

        cell.leave(peer);

        deliver(cell, "°", "", "m");
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
                .setReaction(catchMessage());

        deliver(cell, "°", "foo", "m");
        assertTrue(deliveredToPeer[0]);
    }
}
