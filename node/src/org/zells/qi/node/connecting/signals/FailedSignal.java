package org.zells.qi.node.connecting.signals;

import org.zells.qi.node.connecting.Signal;

public class FailedSignal implements Signal {

    private String reason;

    public FailedSignal() {
    }

    public FailedSignal(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        if (reason == null) {
            return "FAILED";
        }

        return "FAILED " + reason;
    }
}
