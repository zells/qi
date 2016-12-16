package org.zells.qi.apps;

import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.Node;
import org.zells.qi.node.connecting.DefaultChannelFactory;
import org.zells.qi.node.connecting.socket.SocketServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;

abstract class Application {

    final Cell root;
    final Node node;

    Application(String[] args) {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            @Override
            protected String next() {
                return UUID.randomUUID().toString();
            }
        });

        root = new Cell();
        node = new Node(root,
                new Path(Root.name()),
                new SocketServer(determineHost(args), determinePort(args)),
                new DefaultChannelFactory());

        buildModel();
    }

    protected abstract void buildModel();

    private String determineHost(String[] args) {
        return askFor(args, 1, "Host", "localhost");
    }

    private int determinePort(String[] args) {
        return Integer.parseInt(askFor(args, 0, "Port", "42421"));
    }

    private String askFor(String[] args, int index, String name, String defaultValue) {
        if (args.length > index) {
            return args[index];
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(name + " [" + defaultValue + "]: ");

        try {
            String input = reader.readLine();
            if (input.isEmpty()) {
                return defaultValue;
            }
            return input;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return askFor(args, index, name, defaultValue);
        }
    }
}
