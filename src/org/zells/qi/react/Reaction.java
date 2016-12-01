package org.zells.qi.react;

import org.zells.qi.deliver.Delivery;

import java.util.List;

public interface Reaction {

    List<MessageSend> execute(Delivery delivery);
}
