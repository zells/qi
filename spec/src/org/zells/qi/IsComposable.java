package org.zells.qi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IsComposable extends Specification {

    @Test
    void DeliverMessage() {
        ReceivingCell cell = new ReceivingCell();

        deliver(cell, "one", "", "m");
        assertWasDelivered("one( m)");
    }

    @Test
    void DeliverToSelf() {
        ReceivingCell cell = new ReceivingCell();

        deliver(cell, "two", ":.:", "m2");
        assertWasDelivered("two( m2)");
    }

    @Test
    void DeliverToChild() {
        Cell cell = new Cell();
        ReceivingCell child = (ReceivingCell) cell.putChild("foo", new ReceivingCell());

        deliver(cell, child, "one", "foo", "m");
        assertWasDelivered("one.foo( ^.m)");
    }

    @Test
    void NoChildren() {
        Cell cell = new Cell();

        deliver(cell, "", "foo", "");
        assertFalse(wasDelivered);
    }

    @Test
    void NotAChild() {
        Cell cell = new Cell();
        cell.createChild("foo");

        deliver(cell, "", "bar", "");
        assertFalse(wasDelivered);
    }

    @Test
    void DeliverToGrandChild() {
        Cell cell = new Cell();
        Cell foo = cell.createChild("foo");
        ReceivingCell bar = (ReceivingCell) foo.putChild("bar", new ReceivingCell());

        deliver(cell, bar, "one", "foo.bar", "m");
        assertWasDelivered("one.foo.bar( ^.^.m)");
    }

    @Test
    void NotAGrandChild() {
        Cell cell = new Cell();
        cell.createChild("foo");

        deliver(cell, "", "foo.bar", "");
        assertFalse(wasDelivered);
    }

    @Test
    void DeliverToParent() {
        ReceivingCell cell = new ReceivingCell();
        Cell child = cell.createChild("foo");

        deliver(child, cell, "one.foo", "^", "m");
        assertWasDelivered("one( foo.m)");
    }

    @Test
    void NoParent() {
        Cell cell = new Cell();
        deliver(cell, "", "^", "");
        assertFalse(wasDelivered);
    }

    @Test
    void DeliverToGrandParent() {
        ReceivingCell cell = new ReceivingCell();
        Cell child = cell.createChild("foo");
        Cell grandChild = child.createChild("bar");

        deliver(grandChild, cell, "one.foo.bar", "^.^", "m");
        assertWasDelivered("one( foo.bar.m)");
    }

    @Test
    void DeliverToRoot() {
        ReceivingCell cell = new ReceivingCell();
        Cell child = cell.createChild("foo");
        Cell grandChild = child.createChild("bar");

        deliver(grandChild, cell, "one.foo.bar", "°", "m");
        assertWasDelivered("one( foo.bar.m)");
    }

    private String delivered;

    private void deliver(ReceivingCell sendingAndReceiving, String context, String receiver, String message) {
        deliver(sendingAndReceiving, sendingAndReceiving, context, receiver, message);
    }

    private void deliver(Cell sending, ReceivingCell receiving, String context, String receiver, String message) {
        deliver(sending, context, receiver, message);

        if (wasDelivered && receiving != null) {
            delivered = receiving.delivered.toString();
        }
    }

    private void assertWasDelivered(String to) {
        assertTrue(wasDelivered);
        assertEquals(to, delivered);
    }

    private class ReceivingCell extends Cell {
        Delivery delivered;

        @Override
        boolean deliver(Delivery delivery) {
            if (delivery.hasArrived()) {
                delivered = delivery;
                return true;
            }

            return super.deliver(delivery);
        }
    }
}