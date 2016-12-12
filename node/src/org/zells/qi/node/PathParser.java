package org.zells.qi.node;

import org.zells.qi.model.refer.Name;
import org.zells.qi.model.refer.Path;
import org.zells.qi.model.refer.names.Child;
import org.zells.qi.model.refer.names.Parent;
import org.zells.qi.model.refer.names.Root;

public class PathParser {

    public Path parse(String path) {
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
