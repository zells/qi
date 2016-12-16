package org.zells.qi.node.parsing;

import org.zells.qi.model.refer.Name;
import org.zells.qi.model.refer.Path;

import java.util.ArrayList;
import java.util.List;

public class PathPrinter {

    private NamePrinter namePrinter = new NamePrinter();

    public String print(Path path) {
        List<String> printed = new ArrayList<>();

        for (Name name : path.getNames()) {
            printed.add(namePrinter.print(name));
        }

        return String.join(".", printed);
    }
}
