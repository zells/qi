package org.zells.qi.node;

import org.zells.qi.model.Peer;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.node.connecting.Channel;
import org.zells.qi.node.connecting.signals.DeliverSignal;
import org.zells.qi.node.connecting.signals.OkSignal;
import org.zells.qi.node.parsing.SignalPrinter;

public class NodePeer implements Peer {

    private final Channel channel;
    private SignalPrinter printer;

    NodePeer(SignalPrinter printer, Channel channel) {
        this.channel = channel;
        this.printer = printer;
    }

    @Override
    public boolean deliver(Delivery delivery) {
        try {
            String response = channel.send(printer.print(new DeliverSignal(
                    delivery.getContext(),
                    delivery.getTarget(),
                    delivery.getReceiver(),
                    delivery.getMessage(),
                    delivery.getGuid()
            )));

            return response.equals(printer.print(new OkSignal()));
        } catch (Exception e) {
            return false;
        }
    }
}
