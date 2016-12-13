package org.zells.qi.node.fakes;

import org.zells.qi.model.Cell;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Node;

public class FakeNode extends Node {

    public Cell cell;
    public FakeServer server;
    public MessageSend sent;

    public FakeNode() {
        this("incoming", new Cell(), new Path());
    }

    public FakeNode(String connection, Cell cell, Path context) {
        this(new FakeServer(connection), cell, context);
    }

    private FakeNode(FakeServer server, Cell cell, Path context) {
        super(cell, context, server, FakeChannel::new);
        this.server = server;
        this.cell = cell;
    }

    @Override
    public void send(MessageSend send, Runnable onFailed) {
        super.send(send, onFailed);
        sent = send;
    }
}
