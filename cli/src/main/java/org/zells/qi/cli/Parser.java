package org.zells.qi.cli;

import org.zells.qi.model.refer.Path;
import org.zells.qi.node.parsing.Input;
import org.zells.qi.node.parsing.PathParser;

class Parser {

    private PathParser path = new PathParser();

    Path parseReceiver(Input input) {
        Path receiver = path.parse(input);
        input.skip();
        return receiver;
    }

    Path parseMessage(Input input) {
        return path.parse(input);
    }
}
