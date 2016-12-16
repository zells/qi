package org.zells.qi.cli;

import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.parsing.Input;
import org.zells.qi.node.parsing.PathParser;

class Parser {

    private enum State {
        Start, Between, End
    }

    private PathParser path = new PathParser();

    MessageSend parse(Input input) {
        State state = State.Start;

        Path receiver = new Path();
        Path message = new Path();

        while (input.hasNext()) {
            switch (state) {
                case Start:
                    switch (input.current()) {
                        case ' ':
                            state = State.Between;
                            input.skip();
                            break;
                        default:
                            state = State.Between;
                            receiver = path.parse(input);
                    }
                    break;
                case Between:
                    switch (input.current()) {
                        case ' ':
                            state = State.Between;
                            input.skip();
                            break;
                        default:
                            state = State.End;
                            message = path.parse(input);
                    }
                    break;
                default:
                    input.skip();
            }
        }

        return new MessageSend(receiver, message);
    }
}
