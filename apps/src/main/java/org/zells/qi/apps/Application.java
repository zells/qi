package org.zells.qi.apps;

import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.Node;
import org.zells.qi.node.connecting.DefaultChannelFactory;
import org.zells.qi.node.connecting.socket.SocketServer;

import java.io.BufferedReader;
import java.io.IOException;
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
        if (args.length > 1) {
            return args[1];
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Host [localhost]: ");

        try {
            String input = reader.readLine();
            if (input.isEmpty()) {
                return "localhost";
            }
            return input;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return determineHost(args);
        }
    }

    private int determinePort(String[] args) {
        if (args.length > 0) {
            return Integer.parseInt(args[0]);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Port: ");

        try {
            return Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return determinePort(args);
        }
    }
}
