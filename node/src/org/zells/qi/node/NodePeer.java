package org.zells.qi.node;

import org.zells.qi.model.Peer;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.node.connecting.Channel;
import org.zells.qi.node.connecting.Signal;
import org.zells.qi.node.connecting.signals.DeliverSignal;
import org.zells.qi.node.connecting.signals.ReceivedSignal;

public class NodePeer implements Peer {

    private final Channel channel;

    public NodePeer(Channel channel) {
        this.channel = channel;
    }

    @Override
    public boolean deliver(Delivery delivery) {
        try {
            Signal response = channel.send(new DeliverSignal(
                    delivery.getContext(),
                    delivery.getTarget(),
                    delivery.getReceiver(),
                    delivery.getMessage(),
                    delivery.getGuid()
            ));

            return response instanceof ReceivedSignal;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return channel.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NodePeer
                && ((NodePeer) obj).channel.equals(channel);
    }
}
