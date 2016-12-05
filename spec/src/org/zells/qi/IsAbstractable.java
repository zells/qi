package org.zells.qi;

import org.junit.jupiter.api.Test;
import org.zells.qi.react.DynamicReaction;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class IsAbstractable extends Specification {

    @Test
    void NoStem() {
        Cell cell = new Cell();

        deliver(cell, "", "", "");
        assertFalse(wasDelivered);
    }

    @Test
    void NonExistingStem() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        foo.setStem(path("°.bar"));

        deliver(root, "", "", "");
        assertFalse(wasDelivered);
    }

    @Test
    void InheritReaction() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");

        foo.setStem(path("^.bar"));
        bar.setReaction(catchDelivery());

        deliver(root, "r", "foo", "m");
        assertWasDelivered("r.foo( ^.m)");
    }

    @Test
    void ReplaceReaction() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");

        foo.setStem(path("^.bar"));
        bar.setReaction(new DynamicReaction());
        foo.setReaction(catchDelivery());

        deliver(root, "r", "foo", "m");
        assertWasDelivered("r.foo( ^.m)");
    }

    @Test
    void InheritInheritedReaction() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = root.createChild("baz");

        foo.setStem(path("^.bar"));
        bar.setStem(path("^.baz"));
        baz.setReaction(catchDelivery());

        deliver(root, "r", "foo", "m");
        assertWasDelivered("r.foo( ^.m)");
    }

    @Test
    void InheritChild() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = bar.createChild("baz");

        foo.setStem(path("^.bar"));
        baz.setReaction(catchDelivery());

        deliver(root, "r", "foo.baz", "m");
        assertWasDelivered("r.foo.baz( ^.^.m)");
    }

    @Test
    void ReplaceChild() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        bar.createChild("baz");
        Cell newBaz = foo.createChild("baz");

        foo.setStem(path("^.bar"));
        newBaz.setReaction(catchDelivery());

        deliver(root, "r", "foo.baz", "m");
        assertWasDelivered("r.foo.baz( ^.^.m)");
    }

    @Test
    void InheritGrandChild() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = bar.createChild("baz");
        Cell cat = baz.createChild("cat");

        foo.setStem(path("^.bar"));
        cat.setReaction(catchDelivery());

        deliver(root, "r", "foo.baz.cat", "m");
        assertWasDelivered("r.foo.baz.cat( ^.^.^.m)");
    }

    @Test
    void InheritInheritedChild() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = root.createChild("baz");
        Cell cat = baz.createChild("cat");

        foo.setStem(path("^.bar"));
        bar.setStem(path("^.baz"));
        cat.setReaction(catchDelivery());

        deliver(root, "r", "foo.cat", "m");
        assertWasDelivered("r.foo.cat( ^.^.m)");
    }

    @Test
    void SendMessageToStem() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");

        foo.setStem(path("^.bar"));
        bar.setReaction(catchDelivery());
        foo.setReaction((new DynamicReaction()).add(send("*", "n")));

        deliver(root, "r", "foo", "m");
        assertWasDelivered("r.bar( ^.foo.n)");
    }

    @Test
    void InheritFromHigher() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = foo.createChild("baz");

        baz.setStem(path("^.^.bar"));
        bar.setReaction(catchDelivery());

        deliver(root, "r", "foo.baz", "m");
        assertWasDelivered("r.foo.baz( ^.m)");
    }

    @Test
    void InheritFromLower() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = bar.createChild("baz");

        foo.setStem(path("^.bar.baz"));
        baz.setReaction(catchDelivery());

        deliver(root, "r", "foo", "m");
        assertWasDelivered("r.foo( ^.^.m)");
    }

    @Test
    void SelfAsStem() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        foo.setStem(path("^.foo"));

        deliver(root, "r", "foo", "m");
        assertFalse(wasDelivered);
    }

    @Test
    void CircularInheritance() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = root.createChild("baz");

        foo.setStem(path("^.bar"));
        bar.setStem(path("^.baz"));
        baz.setStem(path("^.foo"));

        deliver(root, "r", "foo", "m");
        assertFalse(wasDelivered);
    }

    @Test
    void ChildAsStem() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = foo.createChild("bar");

        foo.setStem(path("bar"));
        bar.setReaction(catchDelivery());

        deliver(root, "r", "foo", "m");
        assertWasDelivered("r.foo( ^.^.m)");
    }
}
