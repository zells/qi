package org.zells.qi.node.singalling.signals;

import org.zells.qi.node.singalling.Signal;

public class FailedSignal implements Signal {

    private String reason = "Unspecified";

    public FailedSignal() {
    }

    public FailedSignal(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "FAILED " + reason;
    }
}
