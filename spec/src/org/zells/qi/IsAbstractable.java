package org.zells.qi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
        foo.setStem(p("°.bar"));

        deliver(root, "", "", "");
        assertFalse(wasDelivered);
    }

    @Test
    void InheritReaction() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");

        foo.setStem(p("^.bar"));
        bar.setReaction(catchDelivery());

        deliver(root, "r", "foo", "m");
        assertWasDelivered("r.foo( ^.m)");
    }

    @Test
    void ReplaceReaction() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");

        foo.setStem(p("^.bar"));
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

        foo.setStem(p("^.bar"));
        bar.setStem(p("^.baz"));
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

        foo.setStem(p("^.bar"));
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

        foo.setStem(p("^.bar"));
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

        foo.setStem(p("^.bar"));
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

        foo.setStem(p("^.bar"));
        bar.setStem(p("^.baz"));
        cat.setReaction(catchDelivery());

        deliver(root, "r", "foo.cat", "m");
        assertWasDelivered("r.foo.cat( ^.^.m)");
    }

    @Test
    void SendMessageToStem() {
        Cell root = new Cell();
        Cell foo = root.createChild("foo");
        Cell bar = root.createChild("bar");

        foo.setStem(p("^.bar"));
        bar.setReaction(catchDelivery());
        foo.setReaction((new DynamicReaction()).add(send("*", "n")));

        deliver(root, "r", "foo", "m");
        assertWasDelivered("r.bar( ^.foo.n)");
    }

    @Test
    @Disabled("TBD")
    void AdoptChild() {
    }

    @Test
    @Disabled("TBD")
    void AdoptGrandChild() {
    }

    @Test
    @Disabled("TBD")
    void AdoptInheritedChild() {
    }

    @Test
    @Disabled("TBD")
    void ChildAsStem() {
    }

    @Test
    @Disabled("TBD")
    void SelfAsStem() {
    }

    @Test
    @Disabled("TBD")
    void CircularInheritance() {
    }
}
