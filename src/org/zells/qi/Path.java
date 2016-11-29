package org.zells.qi;

import java.util.Arrays;
import java.util.List;

class Path {

    private final List<Name> names;

    private Path(List<Name> names) {
        this.names = names;
    }

    Path(Name... names) {
        this.names = Arrays.asList(names);
    }

    Name first() {
        return names.get(0);
    }

    Path rest() {
        return new Path(names.subList(1, names.size()));
    }

    boolean isEmpty() {
        return names.isEmpty();
    }
}
