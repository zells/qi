package org.zells.qi.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zells.qi.cli.fakes.FakeNode;
import org.zells.qi.cli.fakes.FakeUser;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Root;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceivesMessages {

    private FakeUser user;
    private FakeNode node;

    @BeforeEach
    void SetUp() {
        user = new FakeUser();
        node = new FakeNode();
        new CommandLineInterface(user, node);
    }

    @Test
    void EmptyMessage() {
        node.receive(new Path());
        assertEquals("", user.told);
    }
    
    @Test
    void PrintMessage() {
        node.receive(new Path(Child.name("foo")));
        assertEquals("foo", user.told);
    }

    @Test
    void PrintPath() {
        node.receive(new Path(Root.name(), Child.name("foo"), Child.name("bar")));
        assertEquals("°.foo.bar", user.told);
    }
}
