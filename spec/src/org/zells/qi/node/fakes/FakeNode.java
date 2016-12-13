package org.zells.qi.node.fakes;

import org.zells.qi.model.Cell;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Node;

public class FakeNode extends Node {

    public Cell cell;
    public MessageSend sent;

    public FakeNode() {
        this("incoming", new Path());
    }

    public FakeNode(String connection, Path context) {
        this(connection, new Cell(), context);
    }

    private FakeNode(String connection, Cell cell, Path context) {
        super(cell, context, new FakeChannel(connection), FakeChannel::new);
        this.cell = cell;
    }

    @Override
    public void send(MessageSend send) {
        super.send(send);
        sent = send;
    }
}
