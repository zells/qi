package org.zells.qi.node;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.fakes.FakeChannel;
import org.zells.qi.node.fakes.FakeNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConnectsWithPeers {

    @BeforeEach
    void SetUp() {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            @Override
            protected String next() {
                return "global-unique-id";
            }
        });
        FakeChannel.reset();
        node = new FakeNode("fake", new Path(Root.name()));
    }

    @Test
    void JoinPeer() {
        node.join("other");

        assertEquals("JOIN ° fake", FakeChannel.channels.get("other").sent);
    }

    @Test
    void LeavePeer() {
        node.leave("other");

        assertEquals("LEAVE ° fake", FakeChannel.channels.get("other").sent);
    }

    @Test
    void PeerJoins() {
        FakeChannel.channels.get("fake").receive("JOIN °.foo other");

        node.send(new MessageSend(new Path(), new Path(Child.name("m"))));

        assertEquals("DELIVER °  ° °.m global-unique-id", FakeChannel.channels.get("other").sent);
    }

    @Test
    void PeerLeaves() {
        FakeChannel.channels.get("fake").receive("JOIN °.foo other");
        FakeChannel.channels.get("fake").receive("LEAVE °.foo other");

        node.send(new MessageSend(new Path(), new Path(Child.name("m"))));

        assertNull(FakeChannel.channels.get("other").sent);
    }

    private FakeNode node;
}
