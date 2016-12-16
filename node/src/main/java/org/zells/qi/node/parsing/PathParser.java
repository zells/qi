package org.zells.qi.node.parsing;

import org.zells.qi.model.refer.Name;
import org.zells.qi.model.refer.Path;

import java.util.ArrayList;
import java.util.List;

public class PathParser {

    private NameParser name = new NameParser();

    public Path parse(Input input) {
        List<Name> names = new ArrayList<>();

        while (input.hasNext()) {
            switch (input.current()) {
                case ' ':
                    return new Path(names);
                case '.':
                    input.skip();
                    break;
                default:
                    names.add(name.parse(input));
            }
        }

        return new Path(names);
    }
}
