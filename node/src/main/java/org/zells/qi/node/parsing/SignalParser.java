package org.zells.qi.node.parsing;

import org.zells.qi.model.refer.Path;
import org.zells.qi.node.singalling.Signal;
import org.zells.qi.node.singalling.signals.*;

public class SignalParser {

    private PathParser path = new PathParser();

    public Signal parse(Input input) {
        StringBuilder signal = new StringBuilder();
        while (input.hasNext() && input.current() != ' ') {
            signal.append(input.current());
            input.skip();
        }

        input.skip();
        switch (signal.toString().toUpperCase()) {
            case "DELIVER":
                return parseDeliver(input);
            case "JOIN":
                return parseJoin(input);
            case "LEAVE":
                return parseLeave(input);
            case "RECEIVED":
                return new ReceivedSignal();
            case "OK":
                return new OkSignal();
            case "FAILED":
                return parseFailed(input);
            default:
                throw new RuntimeException("Cannot parse signal: " + signal);
        }
    }

    private DeliverSignal parseDeliver(Input input) {
        Path context = path.parse(input);
        input.skip();
        Path target = path.parse(input);
        input.skip();
        Path receiver = path.parse(input);
        input.skip();
        Path message = path.parse(input);
        input.skip();
        String guid = rest(input);

        return new DeliverSignal(
                context,
                target,
                receiver,
                message,
                guid
        );
    }

    private JoinSignal parseJoin(Input input) {
        Path path = this.path.parse(input);
        input.skip();
        String connection = rest(input);

        return new JoinSignal(
                path,
                connection
        );
    }

    private LeaveSignal parseLeave(Input input) {
        Path path = this.path.parse(input);
        input.skip();
        String connection = rest(input);

        return new LeaveSignal(
                path,
                connection
        );
    }

    private FailedSignal parseFailed(Input input) {
        return new FailedSignal(rest(input));
    }

    private String rest(Input input) {
        StringBuilder rest = new StringBuilder();
        while (input.hasNext()) {
            rest.append(input.current());
            input.skip();
        }
        return rest.toString();
    }
}
