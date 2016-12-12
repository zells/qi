package org.zells.qi.cli.fakes;

import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Node;

public class FakeNode extends Node {

    public MessageSend sent;

    @Override
    public void send(MessageSend messageSend) {
        sent = messageSend;
    }

    @Override
    public void receive(Path message) {
        super.receive(message);
    }
}
