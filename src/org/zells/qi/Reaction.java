package org.zells.qi;

import java.util.List;

interface Reaction {

    List<MessageSend> execute(Delivery delivery, Path frame);
}
