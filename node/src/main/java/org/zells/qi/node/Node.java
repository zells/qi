package org.zells.qi.node;

import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.model.deliver.Messenger;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.connecting.ChannelFactory;
import org.zells.qi.node.connecting.Peer;
import org.zells.qi.node.connecting.Server;
import org.zells.qi.node.singalling.Signal;
import org.zells.qi.node.singalling.signals.*;

import java.util.HashMap;
import java.util.Map;

public class Node {

    private final Cell cell;
    private final Path context;
    private final Server server;
    private Map<String, Peer> peers = new HashMap<>();
    private ChannelFactory channels;

    public Node(Cell cell, Path context, Server server, ChannelFactory channels) {
        this.cell = cell;
        this.context = context;
        this.server = server;
        this.channels = channels;

        server.start(new SignalListener());
    }

    public void send(MessageSend send) {
        send(send, () -> {});
    }

    public void send(MessageSend send, Runnable onFailed) {
        (new Messenger(cell, new Delivery(
                context,
                context.with(send.getReceiver()),
                context.with(send.getMessage())
        ))).whenFailed(onFailed).run();
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
        Peer peer = new Peer(channels.forConnection(signal.getConnection()));
        peers.put(signal.getConnection(), peer);
        cell.join(peer);

        return new OkSignal();
    }

    private Signal receive(LeaveSignal signal) {
        cell.leave(peers.get(signal.getConnection()));

        return new OkSignal();
    }

    public void join(String connection) {
        channels.forConnection(connection).send(new JoinSignal(context, server.getConnection()));
    }

    public void leave(String connection) {
        channels.forConnection(connection).send(new LeaveSignal(context, server.getConnection()));
    }

    void stop() {
        server.stop();
    }

    private class SignalListener implements Server.SignalListener {
        @Override
        public Signal receives(Signal signal) {
            return receive(signal);
        }
    }
}
