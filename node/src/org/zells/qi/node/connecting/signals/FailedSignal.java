package org.zells.qi.node.connecting.signals;

import org.zells.qi.node.connecting.Signal;

public class FailedSignal implements Signal {

    @Override
    public String toString() {
        return "FAILED";
    }
}
