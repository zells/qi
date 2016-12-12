package org.zells.qi.cli;

import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Name;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Parent;
import org.zells.qi.model.refer.names.Root;

class Parser {

    MessageSend parse(String messageSend) {
        String[] receiverAndMessage = messageSend.split("\\s");

        Path receiver = parsePath(receiverAndMessage[0]);
        Path message = new Path();

        if (receiverAndMessage.length > 1) {
            message = parsePath(receiverAndMessage[1]);
        }

        return new MessageSend(receiver, message);
    }

    private Path parsePath(String path) {
        if (path.isEmpty() || path.equals(".")) {
            return new Path();
        }

        Path parsed = new Path();
        for (String name : path.split("\\.")) {
            parsed = parsed.with(parseName(name));
        }

        return parsed;
    }

    private Name parseName(String name) {
        switch (name) {
            case "°":
                return Root.name();
            case "^":
                return Parent.name();
            default:
                return Child.name(name);
        }
    }
}
