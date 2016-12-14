package org.zells.qi.model.react;

import org.zells.qi.model.refer.Path;

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

    @Override
    public String toString() {
        return receiver + " <- " + message;
    }

    @Override
    public int hashCode() {
        return receiver.hashCode() + message.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MessageSend
                && ((MessageSend) obj).receiver.equals(receiver)
                && ((MessageSend) obj).message.equals(message);
    }
}
