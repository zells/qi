package org.zells.qi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IsComposableTest extends Specification {

    @Test
    void DeliverMessage() {
        Cell cell = new Cell();
        cell.setReaction(catchMessage());

        deliver(cell, "°", "", "m");
        assertWasReceived("°.m");
    }

    @Test
    void DeliverToChild() {
        Cell cell = new Cell();
        cell.createChild("foo")
                .setReaction(catchMessage());

        deliver(cell, "°", "foo", "m");
        assertWasReceived("°.m");
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
                .setReaction(catchMessage());

        deliver(cell, "°", "foo.bar", "m");
        assertWasReceived("°.m");
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
        cell.setReaction(catchMessage());
        Cell child = cell.createChild("foo");

        deliver(child, "°.foo", "^", "m");
        assertWasReceived("°.foo.m");
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
        cell.setReaction(catchMessage());
        Cell child = cell.createChild("foo");
        Cell grandChild = child.createChild("bar");

        deliver(grandChild, "°.foo.bar", "^.^", "m");
        assertWasReceived("°.foo.bar.m");
    }

    @Test
    void DeliverToRoot() {
        Cell cell = new Cell();
        cell.setReaction(catchMessage());
        Cell grandChild = cell.createChild("foo").createChild("bar");

        deliver(grandChild, "°.foo.bar", "°", "m");
        assertWasReceived("°.foo.bar.m");
    }
}
