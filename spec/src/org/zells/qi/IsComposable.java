package org.zells.qi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IsComposable extends Specification {

    @Test
    void DeliverMessage() {
        Cell cell = new Cell();
        cell.setReaction(catchDelivery());

        deliver(cell, "one", "", "m");
        assertWasDelivered("one( m)");
    }

    @Test
    void DeliverToChild() {
        Cell cell = new Cell();
        cell.createChild("foo")
                .setReaction(catchDelivery());

        deliver(cell, "one", "foo", "m");
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
        cell.createChild("foo")
                .createChild("bar")
                .setReaction(catchDelivery());

        deliver(cell, "one", "foo.bar", "m");
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
        Cell cell = new Cell();
        cell.setReaction(catchDelivery());
        Cell child = cell.createChild("foo");

        deliver(child, "one.foo", "^", "m");
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
        Cell cell = new Cell();
        cell.setReaction(catchDelivery());
        Cell child = cell.createChild("foo");
        Cell grandChild = child.createChild("bar");

        deliver(grandChild, "one.foo.bar", "^.^", "m");
        assertWasDelivered("one( foo.bar.m)");
    }

    @Test
    void DeliverToRoot() {
        Cell cell = new Cell();
        cell.setReaction(catchDelivery());
        Cell grandChild = cell.createChild("foo").createChild("bar");

        deliver(grandChild, "one.foo.bar", "°", "m");
        assertWasDelivered("one( foo.bar.m)");
    }
}