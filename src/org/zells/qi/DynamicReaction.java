package org.zells.qi;

import java.util.ArrayList;
import java.util.List;

class DynamicReaction implements Reaction {

    private List<MessageSend> sends = new ArrayList<>();

    @Override
    public List<MessageSend> execute(Delivery delivery) {
        List<MessageSend> resolvedSends = new ArrayList<>(sends.size());
        for (MessageSend send : sends) {
            resolvedSends.add(send.resolve(delivery));
        }
        return resolvedSends;
    }

    DynamicReaction add(MessageSend send) {
        sends.add(send);
        return this;
    }
}
