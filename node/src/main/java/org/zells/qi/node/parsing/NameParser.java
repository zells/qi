package org.zells.qi.node.parsing;

import org.zells.qi.model.refer.Name;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Parent;
import org.zells.qi.model.refer.names.Root;

class NameParser {

    private enum State {
        Start, Name, QuotedName, EscapeOrEndQuote, EndQuote
    }

    Name parse(Input input) {
        StringBuilder buffer = new StringBuilder();

        State state = State.Start;

        while (input.hasNext()) {
            switch (state) {
                case Start:
                    switch (input.current()) {
                        case '.':
                            return toName(buffer.toString());
                        case ' ':
                            return toName(buffer.toString());
                        case '"':
                            state = State.QuotedName;
                            input.skip();
                            break;
                        default:
                            state = State.Name;
                            buffer.append(input.current());
                            input.skip();
                    }
                    break;
                case Name:
                    switch (input.current()) {
                        case '.':
                            return toName(buffer.toString());
                        case ' ':
                            return toName(buffer.toString());
                        default:
                            buffer.append(input.current());
                            input.skip();
                    }
                    break;
                case QuotedName:
                    switch (input.current()) {
                        case '"':
                            state = State.EscapeOrEndQuote;
                            input.skip();
                            break;
                        default:
                            buffer.append(input.current());
                            input.skip();
                    }
                    break;
                case EscapeOrEndQuote:
                    switch (input.current()) {
                        case '.':
                            return Child.name(buffer.toString());
                        case ' ':
                            return Child.name(buffer.toString());
                        case '"':
                            state = State.QuotedName;
                            buffer.append(input.current());
                            input.skip();
                            break;
                        default:
                            state = State.EndQuote;
                            input.skip();
                    }
                    break;
                case EndQuote:
                    switch (input.current()) {
                        case '.':
                            return Child.name(buffer.toString());
                        case ' ':
                            return Child.name(buffer.toString());
                        default:
                            input.skip();
                    }
                    break;
                default:
                    input.skip();
            }
        }


        switch (state) {
            case QuotedName:
                return Child.name('"' + buffer.toString());
            case EscapeOrEndQuote:
            case EndQuote:
                return Child.name(buffer.toString());
            default:
                return toName(buffer.toString());
        }
    }

    private Name toName(String name) {
        switch (name) {
            case "*":
                return Root.name();
            case "^":
                return Parent.name();
            default:
                return Child.name(name);
        }
    }

}
