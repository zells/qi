package org.zells.qi.node.parsing;

import org.zells.qi.node.singalling.Signal;
import org.zells.qi.node.singalling.signals.DeliverSignal;
import org.zells.qi.node.singalling.signals.JoinSignal;
import org.zells.qi.node.singalling.signals.LeaveSignal;

public class SignalPrinter {

    private PathPrinter path = new PathPrinter();

    public String print(Signal signal) {
        if (signal instanceof DeliverSignal) {
            return print((DeliverSignal) signal);
        } else if (signal instanceof JoinSignal) {
            return print((JoinSignal) signal);
        } else if (signal instanceof LeaveSignal) {
            return print((LeaveSignal) signal);
        } else {
            return signal.toString();
        }
    }

    private String print(DeliverSignal signal) {
        return "DELIVER "
                + path.print(signal.getContext()) + " "
                + path.print(signal.getTarget()) + " "
                + path.print(signal.getReceiver()) + " "
                + path.print(signal.getMessage()) + " "
                + signal.getGuid();
    }

    private String print(JoinSignal signal) {
        return "JOIN "
                + path.print(signal.getPath()) + " "
                + signal.getConnection();
    }

    private String print(LeaveSignal signal) {
        return "LEAVE "
                + path.print(signal.getPath()) + " "
                + signal.getConnection();

    }
}
