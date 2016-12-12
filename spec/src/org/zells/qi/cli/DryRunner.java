package org.zells.qi.cli;

import org.zells.qi.model.react.MessageSend;
import org.zells.qi.node.Node;

public class DryRunner {

    public static void main(String[] args) {
        new CommandLineInterface(new ConsoleUser(), new EchoNode());
    }

    private static class EchoNode extends Node {
        @Override
        public void send(MessageSend messageSend) {
            receive(messageSend.getMessage());
        }
    }
}
