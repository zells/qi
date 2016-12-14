package org.zells.qi.apps;

import org.zells.qi.model.Cell;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;

import java.util.HashSet;
import java.util.Set;

public class Channel extends Application {

    public static void main(String[] args) {
        new Channel(args);
    }

    private Channel(String[] args) {
        super(args);
    }

    private static Set<Path> subscribers = new HashSet<>();

    @Override
    protected void buildModel() {
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
}
