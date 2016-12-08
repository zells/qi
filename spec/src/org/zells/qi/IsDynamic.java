package org.zells.qi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.zells.qi.react.DynamicReaction;

import static org.junit.jupiter.api.Assertions.*;

public class IsDynamic extends Specification {

    @Test
    void NoReaction() {
        Cell cell = new Cell();

        deliver(cell, "", "", "");
        assertFalse(wasDelivered);
    }

    @Test
    void ExecuteReaction() {
        Cell cell = new Cell();
        cell.setReaction(catchMessage());

        deliver(cell, "°", "", "m");
        assertWasReceived("°.m");
    }

    @Test
    void NoMessageSends() {
        Cell cell = new Cell();
        cell.setReaction(new DynamicReaction());

        deliver(cell, "°", "", "m");
        assertTrue(wasDelivered);
    }

    @Test
    void ExecuteMessageSends() {
        Cell cell = new Cell();
        Cell foo = cell.createChild("foo");

        cell.setReaction((new DynamicReaction())
                .add(send("foo", "bar")));
        foo.setReaction(catchMessage());

        deliver(cell, "°", "", "m");
        assertWasReceived("°.bar");
    }

    @Test
    void SendChild() {
        Cell cell = new Cell();
        Cell foo = cell.createChild("foo");
        Cell bar = cell.createChild("bar");

        foo.setReaction((new DynamicReaction())
                .add(send("^.bar", "baz")));
        bar.setReaction(catchMessage());

        deliver(cell, "°", "foo", "m");
        assertWasReceived("°.foo.baz");
    }

    @Test
    void SendSelf() {
        Cell cell = new Cell();
        Cell foo = cell.createChild("foo");
        Cell bar = cell.createChild("bar");

        foo.setReaction((new DynamicReaction())
                .add(send("^.bar", "")));
        bar.setReaction(catchMessage());

        deliver(cell, "°", "foo", "m");
        assertWasReceived("°.foo");
    }

    @Test
    void SendMessage() {
        Cell cell = new Cell();
        Cell foo = cell.createChild("foo");

        cell.setReaction((new DynamicReaction())
                .add(send("foo", "@.baz")));
        foo.setReaction(catchMessage());

        deliver(cell, "°", "", "m");
        assertWasReceived("°.m.baz");
    }

    @Test
    void SendToMessage() {
        Cell cell = new Cell();
        Cell foo = cell.createChild("foo");
        Cell baz = foo.createChild("baz");

        cell.setReaction((new DynamicReaction())
                .add(send("@.baz", "bar")));
        baz.setReaction(catchMessage());

        deliver(cell, "°", "", "foo");
        assertWasReceived("°.bar");
    }

    @Test
    void SendToStem() {
        Cell cell = new Cell();
        Cell foo = cell.createChild("foo");
        Cell bar = cell.createChild("bar");
        Cell baz = bar.createChild("baz");

        foo.setStem(path("^.bar"));
        foo.setReaction((new DynamicReaction())
                .add(send("*.baz", "")));
        baz.setReaction(catchMessage());

        deliver(cell, "°", "foo", "");
        assertWasReceived("°.foo");
    }
}
