package org.zells.qi.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zells.qi.cli.fakes.FakeNode;
import org.zells.qi.cli.fakes.FakeUser;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Parent;
import org.zells.qi.model.refer.names.Root;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SendsMessages {

    private FakeUser user;
    private FakeNode node;

    @BeforeEach
    void SetUp() {
        user = new FakeUser();
        node = new FakeNode();
        new CommandLineInterface(user, node);
    }

    @Test
    void Empty() {
        user.say("");
        assertNull(node.sent);
    }

    @Test
    void SendSelfToSelf() {
        user.say(".");
        assertEquals(new MessageSend(new Path(), new Path()), node.sent);
    }

    @Test
    void SendToChild() {
        user.say("foo");
        assertEquals(new MessageSend(new Path(Child.name("foo")), new Path()), node.sent);
    }

    @Test
    void SendToRoot() {
        user.say("°");
        assertEquals(new MessageSend(new Path(Root.name()), new Path()), node.sent);
    }

    @Test
    void SendToParent() {
        user.say("^");
        assertEquals(new MessageSend(new Path(Parent.name()), new Path()), node.sent);
    }

    @Test
    void SendToPath() {
        user.say("foo.bar");
        assertEquals(new MessageSend(new Path(Child.name("foo"), Child.name("bar")), new Path()), node.sent);
    }

    @Test
    void SendToSelf() {
        user.say(". foo");
        assertEquals(new MessageSend(new Path(), new Path(Child.name("foo"))), node.sent);
    }

    @Test
    void SendImplicitlyToSelf() {
        user.say(" foo");
        assertEquals(new MessageSend(new Path(), new Path(Child.name("foo"))), node.sent);
    }

    @Test
    void SendPathToPath() {
        user.say("^.foo.bar °.baz");
        assertEquals(new MessageSend(
                new Path(Parent.name(), Child.name("foo"), Child.name("bar")),
                new Path(Root.name(), Child.name("baz"))
        ), node.sent);
    }
}
