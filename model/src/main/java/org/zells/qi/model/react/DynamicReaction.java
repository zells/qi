package org.zells.qi.model.react;

import org.zells.qi.model.Cell;
import org.zells.qi.model.deliver.Delivery;
import org.zells.qi.model.deliver.Messenger;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Message;
import org.zells.qi.model.refer.names.Stem;

import java.util.ArrayList;
import java.util.List;

public class DynamicReaction implements Reaction {

    private List<MessageSend> sends = new ArrayList<>();
    private Cell receiver;

    public DynamicReaction(Cell receiver) {
        this.receiver = receiver;
    }

    public DynamicReaction add(MessageSend send) {
        sends.add(send);
        return this;
    }

    @Override
    public void execute(Delivery delivery) {
        for (MessageSend send : sends) {
            Delivery next = delivery.send(
                    resolve(send.getReceiver(), delivery.getMessage()),
                    resolve(send.getMessage(), delivery.getMessage())
            );
            (new Messenger(receiver, next)).run();
        }
    }

    private Path resolve(Path path, Path message) {
        if (path.isEmpty()) {
            return path;
        } else if (path.first().equals(Message.name())) {
            return message.with(path.rest());
        } else if (path.first().equals(Stem.name())) {
            return receiver.getStem().with(path.rest());
        } else {
            return path;
        }
    }
}
