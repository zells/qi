package org.zells.qi;

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
        cell.setReaction(catchDelivery());

        deliver(cell, "one", "", "m");
        assertTrue(wasDelivered);
        assertEquals("one( m)", delivered.toString());
    }

    @Test
    void NoMessageSends() {
        Cell cell = new Cell();
        DynamicReaction reaction = new DynamicReaction();
        cell.setReaction(reaction);

        deliver(cell, "oen", "", "m");
        assertTrue(wasDelivered);
    }

    @Test
    void ExecuteMessageSends() {
        Cell cell = new Cell();
        cell.setReaction((new DynamicReaction())
                .add(send("foo", "bar")));

        Cell foo = cell.createChild("foo");
        foo.setReaction(catchDelivery());

        deliver(cell, "one", "", "m");
        assertWasDelivered("one.foo( ^.bar)");
    }

    @Test
    void SendMessage() {
        Cell cell = new Cell();
        cell.setReaction((new DynamicReaction())
                .add(send("foo", "@.baz")));

        Cell foo = cell.createChild("foo");
        foo.setReaction(catchDelivery());

        deliver(cell, "one", "", "m");
        assertWasDelivered("one.foo( ^.m.baz)");
    }

    @Test
    void SendToMessage() {
        Cell cell = new Cell();
        cell.setReaction((new DynamicReaction())
                .add(send("@.baz", "bar")));

        Cell foo = cell.createChild("foo");
        Cell baz = foo.createChild("baz");
        baz.setReaction(catchDelivery());

        deliver(cell, "one", "", "foo");
        assertWasDelivered("one.foo.baz( ^.^.bar)");
    }
}
