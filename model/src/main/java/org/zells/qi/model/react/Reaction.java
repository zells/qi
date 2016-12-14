package org.zells.qi.model.react;

import org.zells.qi.model.refer.Path;

import java.util.List;

public interface Reaction {

    List<MessageSend> execute(Path message);
}
