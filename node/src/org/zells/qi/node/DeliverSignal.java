package org.zells.qi.node;

import org.zells.qi.model.refer.Path;

public class DeliverSignal implements Signal {
    private Path context;
    private Path target;
    private Path receiver;
    private Path message;
    private String guid;

    public DeliverSignal(Path context, Path target, Path receiver, Path message, String guid) {
        this.context = context;
        this.target = target;
        this.receiver = receiver;
        this.message = message;
        this.guid = guid;
    }

    Path getContext() {
        return context;
    }

    Path getTarget() {
        return target;
    }

    Path getReceiver() {
        return receiver;
    }

    Path getMessage() {
        return message;
    }

    String getGuid() {
        return guid;
    }

    @Override
    public String toString() {
        return "DELIVER " + context + " " + target + " " + receiver + " " + message + " " + guid;
    }
}
