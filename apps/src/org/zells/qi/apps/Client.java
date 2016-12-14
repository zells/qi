package org.zells.qi.apps;

import org.zells.qi.cli.CommandLineInterface;
import org.zells.qi.cli.ConsoleUser;
import org.zells.qi.model.Cell;
import org.zells.qi.node.NodePeer;
import org.zells.qi.node.connecting.DefaultChannelFactory;

public class Client extends Application {

    public static void main(String[] args) {
        new Client(args);
    }

    private Client(String[] args) {
        super(args);
    }

    protected void buildModel() {
        ConsoleUser user = new ConsoleUser();
        Cell cell = root.createChild(Integer.toString(port));
        cell.setReaction(message -> {
            user.tell(message.last().toString());
            return null;
        });

        DefaultChannelFactory channels = new DefaultChannelFactory();
        root.createChild("connect").setReaction(message -> {
            String connection = message.last().toString();

            node.join(connection);
            root.join(new NodePeer(channels.forConnection(connection)));

            return null;
        });

        new CommandLineInterface(user, node);
    }
}
