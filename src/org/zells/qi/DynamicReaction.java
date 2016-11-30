package org.zells.qi;

import java.util.ArrayList;
import java.util.List;

class DynamicReaction implements Reaction {

    private List<MessageSend> sends = new ArrayList<>();

    @Override
    public void execute(Cell cell, Delivery delivery) {
        String frame = GlobalUniqueIdentifierGenerator.generate();
        cell.createChild("#").createChild(frame);

        for (MessageSend send : sends) {
            cell.deliver(delivery.send(
                    resolve(send.getReceiver(), delivery, frame),
                    resolve(send.getMessage(), delivery, frame)));
        }
    }

    private Path resolve(Path path, Delivery delivery, String frame) {
        if (path.first().equals(Message.name())) {
            return delivery.getMessage().with(path.rest());
        } else if (path.first().equals(Frame.name())) {
            return (new Path(Child.name("#"), Child.name(frame))).with(path.rest());
        } else {
            return path;
        }
    }

    DynamicReaction add(MessageSend send) {
        sends.add(send);
        return this;
    }
}
