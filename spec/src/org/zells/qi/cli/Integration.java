package org.zells.qi.cli;

import org.zells.qi.model.Cell;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Node;
import org.zells.qi.node.fakes.FakeChannel;

public class Integration {

    private static CommandLineInterface cli;

    public static void main(String[] args) {
        ConsoleUser user = new ConsoleUser();
        cli = new CommandLineInterface(user, new EchoNode());
    }

    private static class EchoNode extends Node {
        EchoNode() {
            super(new Cell(), new Path(), new FakeChannel("echo"), FakeChannel::new);
        }

        @Override
        public void send(MessageSend messageSend) {
            cli.receive(messageSend.getMessage());
        }
    }
}
