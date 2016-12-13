package org.zells.qi.node.fakes;

import org.zells.qi.model.Cell;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Node;

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
        super(cell, context, channel, connection -> channel);
        this.cell = cell;
        this.channel = channel;
    }

    @Override
    public void send(MessageSend send) {
        super.send(send);
        sent = send;
    }
}
