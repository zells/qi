package org.zells.qi.node;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

public class ConnectOverSocketsTest {

    private Path received;
    private int count = 0;
    private Cell rootTwo;
    private Node two;
    private Node one;

    @BeforeEach
    void SetUp() {
        GlobalUniqueIdentifierGenerator.setGenerator(new GlobalUniqueIdentifierGenerator() {
            int id = 0;

            @Override
            protected String next() {
                return "guid-" + id++;
            }
        });

        one = new Node(
                new Cell(),
                new Path(Root.name()),
                new SocketServer("localhost", 12121),
                new DefaultChannelFactory());

        rootTwo = new Cell();
        two = new Node(
                rootTwo,
                new Path(Root.name()),
                new SocketServer("localhost", 21212),
                new DefaultChannelFactory()
        );
    }

    @AfterEach
    void TearDown() {
        one.stop();
        two.stop();
    }

    @Test
    void SendMessage() {
        rootTwo.setReaction(message -> {
            count++;
            received = message;
            return null;
        });

        two.join("localhost:12121");

        one.send(new MessageSend(new Path(), new Path(Child.name("message"))));

        sleep(50);

        assertEquals(new Path(Root.name(), Child.name("message")), received);
        assertEquals(1, count);
    }

    @Test
    void DoesNotBlockSocket() {
        rootTwo.createChild("foo").setReaction(message -> {
            count++;
            while (received == null) {
                sleep(10);
            }
            return null;
        });
        rootTwo.createChild("bar").setReaction(message -> {
            count++;
            received = message;
            return null;
        });

        two.join("localhost:12121");

        one.send(new MessageSend(new Path(Child.name("foo")), new Path()));
        one.send(new MessageSend(new Path(Child.name("bar")), new Path()));

        sleep(50);

        assertEquals(2, count);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
