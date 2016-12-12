package org.zells.qi.node.signals;

import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Signal;

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

    public Path getContext() {
        return context;
    }

    public Path getTarget() {
        return target;
    }

    public Path getReceiver() {
        return receiver;
    }

    public Path getMessage() {
        return message;
    }

    public String getGuid() {
        return guid;
    }

    @Override
    public String toString() {
        return "DELIVER " + context + " " + target + " " + receiver + " " + message + " " + guid;
    }
}
