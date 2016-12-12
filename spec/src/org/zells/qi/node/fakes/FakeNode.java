package org.zells.qi.node.fakes;

import org.zells.qi.model.Cell;
import org.zells.qi.model.Peer;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.DeliverSignal;
import org.zells.qi.node.Node;
import org.zells.qi.node.OkSignal;
import org.zells.qi.node.PeerFactory;

public class FakeNode extends Node {

    public Cell cell;
    public MessageSend sent;
    public FakeChannel channel;

    public FakeNode() {
        this(new Path());
    }

    public FakeNode(Path context) {
        this(new Cell(), context);
    }

    private FakeNode(Cell cell, Path context) {
        this(cell, context, new FakeChannel());
    }

    private FakeNode(Cell cell, Path context, FakeChannel channel) {
        super(cell, context, channel, new FakePeerFactory(channel));
        this.cell = cell;
        this.channel = channel;
    }

    @Override
    public void send(MessageSend send) {
        super.send(send);
        sent = send;
    }

    private static class FakePeerFactory implements PeerFactory {
        private FakeChannel channel;

        FakePeerFactory(FakeChannel channel) {
            this.channel = channel;
        }

        @Override
        public Peer buildFromConnection(String connection) {
            return new FakePeer();
        }

        private class FakePeer implements Peer {
            @Override
            public boolean deliver(Delivery delivery) {
                return channel.send((new DeliverSignal(
                        delivery.getContext(),
                        delivery.getTarget(),
                        delivery.getReceiver(),
                        delivery.getMessage(),
                        delivery.getGuid()
                )).toString()) instanceof OkSignal;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof FakePeer;
            }

            @Override
            public int hashCode() {
                return 1;
            }
        }
    }
}
