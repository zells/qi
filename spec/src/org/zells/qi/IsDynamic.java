package org.zells.qi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertTrue(wasDelivered);
        assertNotNull(delivered);
        assertEquals("one.foo( ^.bar)", delivered.toString());
    }

    @Test
    void SendMessage() {
        Cell cell = new Cell();
        cell.setReaction((new DynamicReaction())
                .add(send("foo", "@.baz")));

        Cell foo = cell.createChild("foo");
        foo.setReaction(catchDelivery());

        deliver(cell, "one", "", "m");
        assertTrue(wasDelivered);
        assertNotNull(delivered);
        assertEquals("one.foo( ^.m.baz)", delivered.toString());
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
        assertTrue(wasDelivered);
        assertNotNull(delivered);
        assertEquals("one.foo.baz( ^.^.bar)", delivered.toString());
    }

    @Test
    void SendFrame() {
        Cell cell = new Cell();
        cell.setReaction((new DynamicReaction())
                .add(send("foo", "#.bar")));

        cell.createChild("foo")
            .setReaction(catchDelivery());

        deliver(cell, "one", "", "");
        assertTrue(wasDelivered);
        assertNotNull(delivered);
        assertEquals("one.foo( ^.#.1.bar)", delivered.toString());
    }

    @Test
    void SendToFrame() {
        Cell cell = new Cell();
        cell.setReaction((new DynamicReaction())
                .add(send("#.foo", "m")));

        cell.createChild("#")
                .createChild("1")
                .createChild("foo")
                .setReaction(catchDelivery());

        deliver(cell, "one", "", "");
        assertTrue(wasDelivered);
        assertNotNull(delivered);
        assertEquals("one.#.1.foo( ^.^.^.m)", delivered.toString());
    }

    @Test
    void CreateUniqueFrames() {
        Cell cell = new Cell();
        cell.setReaction((new DynamicReaction())
                .add(send("foo", "#.bar")));

        cell.createChild("foo")
                .setReaction(catchDelivery());

        deliver(cell, "one", "", "");
        assertEquals("one.foo( ^.#.1.bar)", delivered.toString());

        deliver(cell, "one", "", "");
        assertEquals("one.foo( ^.#.4.bar)", delivered.toString());
    }
}
