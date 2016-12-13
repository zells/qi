package org.zells.qi.apps;

import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.Node;
import org.zells.qi.node.connecting.DefaultChannelFactory;
import org.zells.qi.node.connecting.socket.SocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Channel {

    private static Set<Path> subscribers = new HashSet<>();

    public static void main(String[] args) throws Exception {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            @Override
            protected String next() {
                return UUID.randomUUID().toString();
            }
        });
        int port = determinePort(args);

        Cell root = new Cell();

        DefaultChannelFactory channels = new DefaultChannelFactory();
        Node node = new Node(root,
                new Path(Root.name()),
                new SocketServer("localhost", port),
                channels);

        Cell cell = root.createChild(Integer.toString(port));
        cell.setReaction(message -> {
            for (Path receiver : subscribers) {
                node.send(new MessageSend(receiver, message),
                        () -> System.out.println("Failed to deliver " + receiver + " <- " + message));
            }
            return null;
        });

        cell.createChild("subscribe").setReaction(message -> {
            subscribers.add(message);
            System.out.println("Subscribed " + message);
            return null;
        });
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
