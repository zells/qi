package org.zells.qi.cli;

import org.zells.qi.model.react.MessageSend;
import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Node;

class CommandLineInterface implements User.InputListener, Node.MessageListener {

    private final User user;
    private final Node node;
    private final Parser parser = new Parser();
    private final Printer printer = new Printer();

    CommandLineInterface(User user, Node node) {
        this.user = user;
        this.node = node;

        user.listen(this);
        node.waitForMessage(this);
    }

    @Override
    public void hears(String input) {
        if (input.isEmpty()) {
            return;
        }

        node.send(parser.parse(input));
    }

    @Override
    public void receives(Path message) {
        user.tell(printer.print(message));
    }
}
