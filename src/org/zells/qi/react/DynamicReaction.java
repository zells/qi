package org.zells.qi.react;

import org.zells.qi.deliver.Delivery;

import java.util.ArrayList;
import java.util.List;

public class DynamicReaction implements Reaction {

    private List<MessageSend> sends = new ArrayList<>();

    @Override
    public List<MessageSend> execute(Delivery delivery) {
        List<MessageSend> resolvedSends = new ArrayList<>(sends.size());
        for (MessageSend send : sends) {
            resolvedSends.add(send.resolve(delivery));
        }
        return resolvedSends;
    }

    public DynamicReaction add(MessageSend send) {
        sends.add(send);
        return this;
    }
}
