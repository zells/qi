package org.zells.qi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsComposable {

    @Test
    void DeliverToSelf() {
        Cell cell = new Cell();
        assertTrue(cell.deliver(new Path(Self.name(), Self.name()), new Path()));
    }

    @Test
    void DeliverToChild() {
        Cell cell = new Cell();
        cell.createChild("foo");
        assertTrue(cell.deliver(new Path(Child.name("foo")), new Path()));
    }

    @Test
    void NoChildren() {
        Cell cell = new Cell();
        assertFalse(cell.deliver(new Path(Child.name("foo")), new Path()));
    }

    @Test
    void NotAChild() {
        Cell cell = new Cell();
        cell.createChild("foo");
        assertFalse(cell.deliver(new Path(Child.name("bar")), new Path()));
    }

    @Test
    void DeliverToGrandChild() {
        Cell cell = new Cell();
        Cell foo = cell.createChild("foo");
        foo.createChild("bar");
        assertTrue(cell.deliver(new Path(Child.name("foo"), Child.name("bar")), new Path()));
    }

    @Test
    void NotAGrandChild() {
        Cell cell = new Cell();
        cell.createChild("foo");
        assertFalse(cell.deliver(new Path(Child.name("foo"), Child.name("bar")), new Path()));
    }

    @Test
    void DeliverToParent() {
        Cell cell = new Cell();
        Cell child = cell.createChild("foo");
        assertTrue(child.deliver(new Path(Parent.name()), new Path()));
    }

    @Test
    void NoParent() {
        Cell cell = new Cell();
        assertFalse(cell.deliver(new Path(Parent.name()), new Path()));
    }

    @Test
    void DeliverToGrandParent() {
        Cell cell = new Cell();
        Cell child = cell.createChild("foo");
        Cell grandChild = child.createChild("bar");
        assertTrue(grandChild.deliver(new Path(Parent.name(), Parent.name()), new Path()));
    }

    @Test
    void DeliverToRoot() {
        Cell cell = new Cell();
        Cell child = cell.createChild("foo");
        Cell grandChild = child.createChild("bar");
        assertTrue(grandChild.deliver(new Path(Root.name()), new Path()));
    }
}