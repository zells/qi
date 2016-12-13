package org.zells.qi.node;

import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.model.deliver.Messenger;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.connecting.Channel;
import org.zells.qi.node.connecting.ChannelFactory;
import org.zells.qi.node.connecting.Signal;
import org.zells.qi.node.connecting.signals.*;
import org.zells.qi.node.parsing.SignalParser;
import org.zells.qi.node.parsing.SignalPrinter;

import java.util.HashMap;
import java.util.Map;

public class Node {

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

    private Signal receive(Signal signal) {
        if (signal instanceof DeliverSignal) {
            return receive((DeliverSignal) signal);
        } else if (signal instanceof JoinSignal) {
            return receive((JoinSignal) signal);
        } else if (signal instanceof LeaveSignal) {
            return receive((LeaveSignal) signal);
        } else {
            throw new RuntimeException("Unknown signal: " + signal.getClass());
        }
    }

    private Signal receive(DeliverSignal signal) {
        boolean received = cell.deliver(new Delivery(
                signal.getContext(),
                signal.getTarget(),
                signal.getReceiver(),
                signal.getMessage(),
                signal.getGuid()));

        if (received) {
            return new ReceivedSignal();
        } else {
            return new FailedSignal();
        }
    }

    private Signal receive(JoinSignal signal) {
        NodePeer peer = new NodePeer(printer, channels.forConnection(signal.getConnection()));
        peers.put(signal.getConnection(), peer);
        cell.join(peer);

        return new OkSignal();
    }

    private Signal receive(LeaveSignal signal) {
        cell.leave(peers.get(signal.getConnection()));

        return new OkSignal();
    }

    public void join(String connection) {
        channels.forConnection(connection).send(printer.print(new JoinSignal(context, channel.getConnection())));
    }

    public void leave(String connection) {
        channels.forConnection(connection).send(printer.print(new LeaveSignal(context, channel.getConnection())));
    }

    void stop() {
        channel.close();
    }

    private class SignalListener implements Channel.SignalListener {
        @Override
        public String receives(String signal) {
            return printer.print(receive(parser.parse(signal)));
        }
    }
}
