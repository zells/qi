package org.zells.qi.cli;

import org.zells.qi.model.refer.Path;
import org.zells.qi.node.Node;
import org.zells.qi.node.parsing.Input;

public class CommandLineInterface {

    private final User user;
    private final Node node;
    private final Parser parser = new Parser();
    private final Printer printer = new Printer();

    public CommandLineInterface(User user, Node node) {
        this.user = user;
        this.node = node;

        user.listen(new InputListener());
    }

    void receive(Path message) {
        user.tell(printer.print(message));
    }

    private class InputListener implements User.InputListener {
        @Override
        public void hears(String string) {
            if (string.isEmpty()) {
                return;
            }

            Input input = new Input(string);
            node.send(parser.parseReceiver(input), parser.parseMessage(input),
                    () -> user.tell("## Failed to deliver " + string));
        }
    }
}
