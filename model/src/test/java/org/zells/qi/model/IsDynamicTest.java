package org.zells.qi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class IsDynamicTest extends Specification {

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

        deliver(cell, "*", "", "m");
        assertWasReceived("*.m");
    }
}
