package org.zells.qi.model.react;

import org.zells.qi.model.refer.Path;

import java.util.ArrayList;
import java.util.List;

public class DynamicReaction implements Reaction {

    private List<MessageSend> sends = new ArrayList<>();

    @Override
    public List<MessageSend> execute(Path message) {
        return sends;
    }

    public DynamicReaction add(MessageSend send) {
        sends.add(send);
        return this;
    }
}
