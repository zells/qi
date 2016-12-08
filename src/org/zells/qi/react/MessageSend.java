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
}
