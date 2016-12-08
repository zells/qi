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
        bar.setReaction(catchMessage());

        deliver(root, "°", "foo", "m");
        assertWasReceived("°.m");
    }

    @Test
    void ReplaceReaction() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");

        foo.setStem(path("^.bar"));
        bar.setReaction(new DynamicReaction());
        foo.setReaction(catchMessage());

        deliver(root, "°", "foo", "m");
        assertWasReceived("°.m");
    }

    @Test
    void InheritInheritedReaction() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = root.createChild("baz");

        foo.setStem(path("^.bar"));
        bar.setStem(path("^.baz"));
        baz.setReaction(catchMessage());

        deliver(root, "°", "foo", "m");
        assertWasReceived("°.m");
    }

    @Test
    void InheritChild() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = bar.createChild("baz");

        foo.setStem(path("^.bar"));
        baz.setReaction(catchMessage());

        deliver(root, "°", "foo.baz", "m");
        assertWasReceived("°.m");
    }

    @Test
    void ReplaceChild() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        bar.createChild("baz");
        Cell newBaz = foo.createChild("baz");

        foo.setStem(path("^.bar"));
        newBaz.setReaction(catchMessage());

        deliver(root, "°", "foo.baz", "m");
        assertWasReceived("°.m");
    }

    @Test
    void InheritGrandChild() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = bar.createChild("baz");
        Cell cat = baz.createChild("cat");

        foo.setStem(path("^.bar"));
        cat.setReaction(catchMessage());

        deliver(root, "°", "foo.baz.cat", "m");
        assertWasReceived("°.m");
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
        cat.setReaction(catchMessage());

        deliver(root, "°", "foo.cat", "m");
        assertWasReceived("°.m");
    }

    @Test
    void InheritFromHigher() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = foo.createChild("baz");

        baz.setStem(path("^.^.bar"));
        bar.setReaction(catchMessage());

        deliver(root, "°", "foo.baz", "m");
        assertWasReceived("°.m");
    }

    @Test
    void InheritFromLower() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = bar.createChild("baz");

        foo.setStem(path("^.bar.baz"));
        baz.setReaction(catchMessage());

        deliver(root, "°", "foo", "m");
        assertWasReceived("°.m");
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
        bar.setReaction(catchMessage());

        deliver(root, "°", "foo", "m");
        assertWasReceived("°.m");
    }

    @Test
    void ResolveRelativePaths() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = root.createChild("baz");

        foo.setStem(path("^.bar"));
        bar.setReaction((new DynamicReaction())
                .add(send("^.baz", "cat")));
        baz.setReaction(catchMessage());

        deliver(root, "°", "foo", "m");

        assertWasReceived("°.foo.cat");
    }

    @Test
    void ResolveAbsolutePath() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = root.createChild("baz");

        foo.setStem(path("^.bar"));
        bar.setReaction((new DynamicReaction())
                .add(send("^.baz", "°.bar")));
        baz.setReaction(catchMessage());

        deliver(root, "°", "foo", "m");

        assertWasReceived("°.bar");
    }

    @Test
    void SendToChildOfInheritingCell() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");
        Cell baz = foo.createChild("baz");

        foo.setStem(path("^.bar"));
        bar.setReaction((new DynamicReaction())
                .add(send("baz", "cat")));
        baz.setReaction(catchMessage());

        deliver(root, "°", "foo", "m");

        assertWasReceived("°.foo.cat");
    }
}
