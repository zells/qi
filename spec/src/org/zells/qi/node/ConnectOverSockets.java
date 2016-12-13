package org.zells.qi.node;

import org.junit.jupiter.api.Test;
import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.GlobalUniqueIdentifierGenerator;
import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Root;
import org.zells.qi.node.connecting.DefaultChannelFactory;
import org.zells.qi.node.connecting.socket.SocketServer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectOverSockets {

    private Path received;
    private int count = 0;

    @Test
    void SendMessage() throws InterruptedException {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            int id = 0;

            @Override
            protected String next() {
                return "guid-" + id++;
            }
        });

        Node one = new Node(
                new Cell(),
                new Path(Root.name()),
                new SocketServer("localhost", 42421),
                new DefaultChannelFactory());

        Cell rootTwo = new Cell();
        rootTwo.setReaction(message -> {
            count++;
            received = message;
            return null;
        });

        Node two = new Node(
                rootTwo,
                new Path(Root.name()),
                new SocketServer("localhost", 42422),
                new DefaultChannelFactory()
        );

        two.join("localhost:42421");

        one.send(new MessageSend(new Path(), new Path(Child.name("message"))));

        Thread.sleep(50);

        one.stop();
        two.stop();

        assertEquals(new Path(Root.name(), Child.name("message")), received);
        assertEquals(1, count);
    }
}
