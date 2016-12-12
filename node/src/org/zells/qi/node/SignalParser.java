package org.zells.qi.node;

class SignalParser {
    private PathParser path = new PathParser();

    Signal parse(String signal) {
        String[] parts = signal.split(" ");

        switch (parts[0]) {
            case "DELIVER":
                return new DeliverSignal(
                        path.parse(parts[1]),
                        path.parse(parts[2]),
                        path.parse(parts[3]),
                        path.parse(parts[4]),
                        parts[5]
                );
            case "JOIN":
                return new JoinSignal(
                        path.parse(parts[1]),
                        parts[2]);
            case "LEAVE":
                return new LeaveSignal(
                        path.parse(parts[1]),
                        parts[2]
                );
            default:
                throw new RuntimeException("Cannot parse signal: " + signal);
        }
    }
}
