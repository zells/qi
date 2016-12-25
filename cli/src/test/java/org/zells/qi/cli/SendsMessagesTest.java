package org.zells.qi.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zells.qi.cli.fakes.FakeUser;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Parent;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.fakes.FakeNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SendsMessagesTest {

    private FakeUser user;
    private FakeNode node;

    @BeforeEach
    void SetUp() {
        user = new FakeUser();
        node = new FakeNode();
        new CommandLineInterface(user, node);

        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            @Override
            protected String next() {
                return "global-unique-id";
            }
        });
    }

    @Test
    void Empty() {
        user.say("");
        assertNull(node.sentTo);
    }

    @Test
    void SendSelfToSelf() {
        user.say(".");
        assertEquals(new Path(), node.sentTo);
        assertEquals(new Path(), node.sentMessage);
    }

    @Test
    void SendToChild() {
        user.say("foo");
        assertEquals(new Path(Child.name("foo")), node.sentTo);
    }

    @Test
    void SendToRoot() {
        user.say("*");
        assertEquals(new Path(Root.name()), node.sentTo);
    }

    @Test
    void SendToParent() {
        user.say("^");
        assertEquals(new Path(Parent.name()), node.sentTo);
    }

    @Test
    void SendToPath() {
        user.say("foo.bar");
        assertEquals(new Path(Child.name("foo"), Child.name("bar")), node.sentTo);
    }

    @Test
    void SendToSelf() {
        user.say(". foo");
        assertEquals(new Path(), node.sentTo);
        assertEquals(new Path(Child.name("foo")), node.sentMessage);
    }

    @Test
    void SendImplicitlyToSelf() {
        user.say(" foo");
        assertEquals(new Path(), node.sentTo);
        assertEquals(new Path(Child.name("foo")), node.sentMessage);
    }

    @Test
    void SendPathToPath() {
        user.say("^.foo.bar *.baz");
        assertEquals(new Path(Parent.name(), Child.name("foo"), Child.name("bar")), node.sentTo);
        assertEquals(new Path(Root.name(), Child.name("baz")), node.sentMessage);
    }

    @Test
    void DotInName() {
        user.say("\"foo.bar\".baz");
        assertEquals(new Path(Child.name("foo.bar"), Child.name("baz")), node.sentTo);
    }

    @Test
    void SpaceInName() {
        user.say("\"foo bar\".baz");
        assertEquals(new Path(Child.name("foo bar"), Child.name("baz")), node.sentTo);
    }

    @Test
    void QuoteInName() {
        user.say("foo\"bar.baz");
        assertEquals(new Path(Child.name("foo\"bar"), Child.name("baz")), node.sentTo);
    }

    @Test
    void QuoteInQuotedName() {
        user.say("\"foo\"\"bar\".baz");
        assertEquals(new Path(Child.name("foo\"bar"), Child.name("baz")), node.sentTo);
    }

    @Test
    void QuotedRoot() {
        user.say("\"*\"");
        assertEquals(new Path(Child.name("*")), node.sentTo);
    }
}
