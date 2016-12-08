package org.zells.qi.react;

import org.zells.qi.refer.Path;

import java.util.List;

public interface Reaction {

    List<MessageSend> execute(Path message);
}
