package org.zells.qi.cli;

import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.PathParser;

class Parser {

    private PathParser path = new PathParser();

    MessageSend parse(String messageSend) {
        String[] receiverAndMessage = messageSend.split("\\s");

        Path receiver = path.parse(receiverAndMessage[0]);
        Path message = new Path();

        if (receiverAndMessage.length > 1) {
            message = path.parse(receiverAndMessage[1]);
        }

        return new MessageSend(receiver, message);
    }
}
