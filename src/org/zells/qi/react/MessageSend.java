package org.zells.qi.react;

import org.zells.qi.deliver.Delivery;
import org.zells.qi.refer.Path;
import org.zells.qi.refer.names.Message;

public class MessageSend {

    private final Path receiver;
    private final Path message;

    public MessageSend(Path receiver, Path message) {
        this.receiver = receiver;
        this.message = message;
    }

    public Path getReceiver() {
        return receiver;
    }

    public Path getMessage() {
        return message;
    }

    MessageSend resolve(Delivery delivery) {
        return new MessageSend(resolve(receiver, delivery), resolve(message, delivery));
    }

    private Path resolve(Path path, Delivery delivery) {
        if (path.first().equals(Message.name())) {
            return delivery.getMessage().with(path.rest());
        } else {
            return path;
        }
    }
}
