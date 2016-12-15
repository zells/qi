package org.zells.qi.node.connecting;

import org.zells.qi.model.Courier;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.node.singalling.Signal;
import org.zells.qi.node.singalling.signals.DeliverSignal;
import org.zells.qi.node.singalling.signals.ReceivedSignal;

public class Peer implements Courier {

    private final Channel channel;

    public Peer(Channel channel) {
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
        return obj instanceof Peer
                && ((Peer) obj).channel.equals(channel);
    }
}
