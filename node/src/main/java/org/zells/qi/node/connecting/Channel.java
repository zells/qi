package org.zells.qi.node.connecting;

import org.zells.qi.node.singalling.Signal;

public interface Channel {

    Signal send(Signal signal);
}
