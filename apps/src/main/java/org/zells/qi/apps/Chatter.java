package org.zells.qi.apps;

import org.zells.qi.cli.CommandLineInterface;
import org.zells.qi.cli.ConsoleUser;
import org.zells.qi.model.Cell;
import org.zells.qi.model.Courier;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.react.Reaction;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.connecting.Peer;
import org.zells.qi.node.connecting.DefaultChannelFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chatter extends Application {

    public static void main(String[] args) {
        new Chatter(args);
    }

    private Chatter(String[] args) {
        super(args);
    }

    protected void buildModel() {
        ConsoleUser user = new ConsoleUser();

        Cell tell = root.createChild("user");
        Cell re = root.createChild("topic");

        root.createChild("connect").setReaction(new ConnectReaction());
        root.createChild("iam").setReaction(new IAmReaction(user, tell));
        root.createChild("open").setReaction(new OpenReaction(re));

        new CommandLineInterface(user, node);
    }

    private class ConnectReaction implements Reaction {

        private DefaultChannelFactory channels = new DefaultChannelFactory();

        @Override
        public List<MessageSend> execute(Path message) {
            String connection = message.last().toString();

            node.join(connection);
            root.join(new Peer(channels.forConnection(connection)));

            return null;
        }
    }

    private class IAmReaction implements Reaction {

        private final ConsoleUser user;
        private final Cell tell;

        IAmReaction(ConsoleUser user, Cell tell) {
            this.user = user;
            this.tell = tell;
        }

        @Override
        public List<MessageSend> execute(Path message) {
            String name = message.last().toString();
            tell.createChild(name).setReaction(new ChatterReaction(user));
            return null;
        }
    }

    private class OpenReaction implements Reaction {

        private Cell re;

        OpenReaction(Cell re) {
            this.re = re;
        }

        @Override
        public List<MessageSend> execute(Path message) {
            re.putChild(message.last().toString(), new TopicCell());
            return null;
        }
    }

    private class ChatterReaction implements Reaction {

        private ConsoleUser user;

        ChatterReaction(ConsoleUser user) {
            this.user = user;
        }

        @Override
        public List<MessageSend> execute(Path message) {
            user.tell(message.last().toString());
            return null;
        }
    }

    private class TopicCell extends Cell {

        private Set<Path> subscribers = new HashSet<>();

        TopicCell() {
            super();
            createChild("subscribe").setReaction(message -> {
                subscribers.add(message);
                return null;
            });

            setReaction(message -> {
                for (Path receiver : subscribers) {
                    node.send(new MessageSend(receiver, message));
                }
                return null;
            });
        }
    }
}
