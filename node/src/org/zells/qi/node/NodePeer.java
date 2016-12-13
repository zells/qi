package org.zells.qi.node;

import org.zells.qi.model.Peer;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.node.parsing.SignalPrinter;
import org.zells.qi.node.signals.DeliverSignal;
import org.zells.qi.node.signals.OkSignal;

public class NodePeer implements Peer {

    private final Channel channel;
    private SignalPrinter printer;

    NodePeer(SignalPrinter printer, Channel channel) {
        this.channel = channel;
        this.printer = printer;
    }

    @Override
    public boolean deliver(Delivery delivery) {
        return channel.send(printer.print(new DeliverSignal(
                delivery.getContext(),
                delivery.getTarget(),
                delivery.getReceiver(),
                delivery.getMessage(),
                delivery.getGuid()
        ))) instanceof OkSignal;
    }
}
