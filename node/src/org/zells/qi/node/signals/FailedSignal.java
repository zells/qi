package org.zells.qi.node.signals;

import org.zells.qi.node.Signal;

public class FailedSignal implements Signal {

    @Override
    public String toString() {
        return "FAILED";
    }
}
