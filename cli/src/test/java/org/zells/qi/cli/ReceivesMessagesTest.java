package org.zells.qi.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.zells.qi.cli.fakes.FakeUser;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.fakes.FakeNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceivesMessagesTest {

    private FakeUser user;
    private FakeNode node;
    private CommandLineInterface cli;

    @BeforeEach
    void SetUp() {
        user = new FakeUser();
        node = new FakeNode();
        cli = new CommandLineInterface(user, node);
    }

    @Test
    void EmptyMessage() {
        deliver(new Path());
        assertEquals("", user.told);
    }

    @Test
    void PrintMessage() {
        deliver(new Path(Child.name("foo")));
        assertEquals("foo", user.told);
    }

    @Test
    void PrintPath() {
        deliver(new Path(Root.name(), Child.name("foo"), Child.name("bar")));
        assertEquals("°.foo.bar", user.told);
    }

    @Test
    void DotInName() {
        deliver(new Path(Child.name("foo.bar")));
        assertEquals("\"foo.bar\"", user.told);
    }

    @Test
    void SpaceInName() {
        deliver(new Path(Child.name("foo bar")));
        assertEquals("\"foo bar\"", user.told);
    }

    @Test
    void QuoteInName() {
        deliver(new Path(Child.name("foo \"bar")));
        assertEquals("\"foo \"\"bar\"", user.told);
    }

    private void deliver(Path message) {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            @Override
            protected String next() {
                return "foo";
            }
        });
        node.cell.setReaction(m -> {
            cli.receive(m);
            return null;
        });
        node.cell.deliver(new Delivery(new Path(), new Path(), message));
    }
}
