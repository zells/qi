package org.zells.qi.node;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Root;
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
        node = new FakeNode(new Path(Root.name()));
    }

    @Test
    void JoinPeer() {
        node.join("<connection>");

        assertEquals("JOIN ° <connection>", node.channel.sent);
    }

    @Test
    void LeavePeer() {
        node.leave("<connection>");

        assertEquals("LEAVE ° <connection>", node.channel.sent);
    }

    @Test
    void PeerJoins() {
        node.channel.receive("JOIN °.foo <connection>");

        node.send(new MessageSend(new Path(), new Path(Child.name("m"))));

        assertEquals("DELIVER °  ° °.m global-unique-id", node.channel.sent);
    }

    @Test
    void PeerLeaves() {
        node.channel.receive("JOIN °.foo <connection>");
        node.channel.receive("LEAVE °.foo <connection>");

        node.send(new MessageSend(new Path(), new Path(Child.name("m"))));

        assertNull(node.channel.sent);
    }

    private FakeNode node;
}
