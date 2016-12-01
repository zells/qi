package org.zells.qi;

class MessageSend {

    private final Path receiver;
    private final Path message;

    MessageSend(Path receiver, Path message) {
        this.receiver = receiver;
        this.message = message;
    }

    Path getReceiver() {
        return receiver;
    }

    Path getMessage() {
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
