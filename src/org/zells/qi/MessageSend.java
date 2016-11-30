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
}
