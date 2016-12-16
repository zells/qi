package org.zells.qi.node.parsing;

import org.zells.qi.model.refer.Name;
import org.zells.qi.model.refer.names.Child;

class NamePrinter {

    String print(Name name) {
        String printed = name.toString();

        if (name instanceof Child) {
            if (printed.contains(" ") || printed.contains(".")) {
                return '"' + printed.replaceAll("\"", "\"\"") + '"';
            }
        }

        return printed;
    }
}
