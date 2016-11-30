package org.zells.qi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SameParameterValue")
public class IsComposable {

    private boolean wasDelivered;
    private String delivered;

    @Test
    void DeliverMessage() {
        ReceivingCell cell = new ReceivingCell();

        deliver(cell, "one", "", "m");
        assertWasDelivered("one( m)");
    }

    @Test
    void DeliverToSelf() {
        ReceivingCell cell = new ReceivingCell();

        deliver(cell, "one", ":.:", "m");
        assertWasDelivered("one( m)");
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

    private void assertWasDelivered(String to) {
        assertTrue(wasDelivered);
        assertEquals(to, delivered);
    }

    private void deliver(ReceivingCell sendingAndReceiving, String context, String receiver, String message) {
        deliver(sendingAndReceiving, sendingAndReceiving, context, receiver, message);
    }

    private void deliver(Cell sending, String context, String receiver, String message) {
        deliver(sending, null, context, receiver, message);
    }

    private void deliver(Cell sending, ReceivingCell receiving, String context, String receiver, String message) {
        wasDelivered = sending.deliver(new Delivery(parse(context), parse(receiver), parse(message)));

        if (wasDelivered && receiving != null) {
            delivered = receiving.delivered.toString();
        }
    }

    private Path parse(String string) {
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
            case ":":
                return Self.name();
            case "^":
                return Parent.name();
            case "°":
                return Root.name();
            default:
                return Child.name(string);
        }
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