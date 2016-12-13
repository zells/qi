package org.zells.qi.apps;

import org.zells.qi.cli.CommandLineInterface;
import org.zells.qi.cli.ConsoleUser;
import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.Node;
import org.zells.qi.node.NodePeer;
import org.zells.qi.node.connecting.DefaultChannelFactory;
import org.zells.qi.node.connecting.socket.SocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class Client {

    public static void main(String[] args) throws Exception {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            @Override
            protected String next() {
                return UUID.randomUUID().toString();
            }
        });
        int port = determinePort(args);

        Cell root = new Cell();

        ConsoleUser user = new ConsoleUser();
        DefaultChannelFactory channels = new DefaultChannelFactory();
        Node node = new Node(root,
                new Path(Root.name()),
                new SocketServer("localhost", port),
                channels);

        Cell cell = root.createChild(Integer.toString(port));
        cell.setReaction(message -> {
            user.tell("");
            user.tell(message.last().toString());
            System.out.print("> ");
            return null;
        });

        root.createChild("connect").setReaction(message -> {
            String connection = message.last().toString();

            node.join(connection);
            root.join(new NodePeer(channels.forConnection(connection)));

            return null;
        });

        new CommandLineInterface(user, node);
    }

    private static int determinePort(String[] args) throws IOException {
        if (args.length > 0) {
            return Integer.parseInt(args[0]);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Port: ");
        return Integer.parseInt(reader.readLine());
    }
}
