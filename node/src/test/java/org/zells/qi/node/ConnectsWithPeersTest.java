package org.zells.qi.node;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.connecting.signals.DeliverSignal;
import org.zells.qi.node.connecting.signals.JoinSignal;
import org.zells.qi.node.connecting.signals.LeaveSignal;
import org.zells.qi.node.fakes.FakeChannel;
import org.zells.qi.node.fakes.FakeNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConnectsWithPeersTest {

    @BeforeEach
    void SetUp() {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            @Override
            protected String next() {
                return "global-unique-id";
            }
        });
        FakeChannel.reset();
        node = new FakeNode("fake", (new Cell()).createChild("foo"), new Path(Root.name(), Child.name("foo")));
    }

    @Test
    void JoinPeer() {
        node.join("other");

        assertEquals(new JoinSignal(
                new Path(Root.name(), Child.name("foo")),
                "fake"
        ), FakeChannel.channels.get("other").sent);
    }

    @Test
    void LeavePeer() {
        node.leave("other");

        assertEquals(new LeaveSignal(
                new Path(Root.name(), Child.name("foo")),
                "fake"
        ), FakeChannel.channels.get("other").sent);
    }

    @Test
    void PeerJoins() throws InterruptedException {
        node.server.receive(new JoinSignal(new Path(Root.name(), Child.name("foo")), "other"));

        node.send(new MessageSend(new Path(Child.name("bar")), new Path(Child.name("m"))));

        while (FakeChannel.channels.get("other").sent == null) {
            Thread.sleep(10);
        }

        assertEquals(new DeliverSignal(
                new Path(Root.name(), Child.name("foo")),
                new Path(Child.name("bar")),
                new Path(Root.name(), Child.name("foo"), Child.name("bar")),
                new Path(Root.name(), Child.name("foo"), Child.name("m")),
                "global-unique-id"
        ), FakeChannel.channels.get("other").sent);
    }

    @Test
    void PeerLeaves() {
        node.server.receive(new JoinSignal(new Path(Root.name(), Child.name("foo")), "other"));
        node.server.receive(new LeaveSignal(new Path(Root.name(), Child.name("foo")), "other"));

        node.send(new MessageSend(new Path(), new Path(Child.name("m"))));

        assertNull(FakeChannel.channels.get("other").sent);
    }

    private FakeNode node;
}
