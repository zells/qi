package org.zells.qi.cli;

import org.zells.qi.model.refer.Path;
import org.zells.qi.node.parsing.PathPrinter;

class Printer {

    private PathPrinter printer = new PathPrinter();

    String print(Path path) {
        return printer.print(path);
    }
}
