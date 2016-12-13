package org.zells.qi.node;

import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.model.deliver.Messenger;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.parsing.SignalParser;
import org.zells.qi.node.parsing.SignalPrinter;
import org.zells.qi.node.signals.*;

import java.util.HashMap;
import java.util.Map;

public abstract class Node {

    private final Cell cell;
    private final Path context;
    private final Channel channel;
    private final SignalPrinter printer = new SignalPrinter();
    private final SignalParser parser = new SignalParser();
    private Map<String, NodePeer> peers = new HashMap<>();
    private ChannelFactory channels;

    public Node(Cell cell, Path context, Channel channel, ChannelFactory channels) {
        this.cell = cell;
        this.context = context;
        this.channel = channel;
        this.channels = channels;

        channel.listen(new SignalListener());
    }

    public void send(MessageSend send) {
        (new Messenger(cell, new Delivery(
                context,
                context.with(send.getReceiver()),
                context.with(send.getMessage())
        ))).run();
    }

    private void receive(Signal signal) {
        if (signal instanceof DeliverSignal) {
            receive((DeliverSignal) signal);
        } else if (signal instanceof JoinSignal) {
            receive((JoinSignal) signal);
        } else if (signal instanceof LeaveSignal) {
            receive((LeaveSignal) signal);
        } else {
            throw new RuntimeException("Unknown signal: " + signal.getClass());
        }
    }

    private void receive(DeliverSignal signal) {
        boolean received = cell.deliver(new Delivery(
                signal.getContext(),
                signal.getTarget(),
                signal.getReceiver(),
                signal.getMessage(),
                signal.getGuid()));

        if (received) {
            channel.send(printer.print(new ReceivedSignal()));
        } else {
            channel.send(printer.print(new FailedSignal()));
        }
    }

    private void receive(JoinSignal signal) {
        NodePeer peer = new NodePeer(printer, channels.forConnection(signal.getConnection()));
        peers.put(signal.getConnection(), peer);
        cell.join(peer);
    }

    private void receive(LeaveSignal signal) {
        cell.leave(peers.get(signal.getConnection()));
    }

    public void join(String connection) {
        channel.send(printer.print(new JoinSignal(context, connection)));
    }

    public void leave(String connection) {
        channel.send(printer.print(new LeaveSignal(context, connection)));
    }

    private class SignalListener implements Channel.SignalListener {
        @Override
        public void receives(String signal) {
            receive(parser.parse(signal));
        }
    }
}
