package org.zells.qi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Path {

    private final List<Name> names;

    private Path(List<Name> names) {
        this.names = names;
    }

    Path(Name... names) {
        this.names = Arrays.asList(names);
    }

    Name last() {
        return names.get(names.size() - 1);
    }

    Name first() {
        return names.get(0);
    }

    Path rest() {
        return new Path(names.subList(1, names.size()));
    }

    Path in(Name name) {
        return in(new Path(name));
    }

    private Path in(Path context) {
        return context.with(this);
    }

    private Path with(Path path) {
        Path newPath = this;
        for (Name n : path.names) newPath = newPath.with(n);
        return newPath;
    }

    Path with(Name name) {
        List<Name> newNames = new ArrayList<>(names.size() + 1);
        for (Name n : names) newNames.add(n);
        newNames.add(name);

        return new Path(newNames);
    }

    Path up() {
        return new Path(names.subList(0, names.size() - 1));
    }

    boolean isEmpty() {
        return names.isEmpty();
    }

    @Override
    public int hashCode() {
        return names.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Path && names.equals(((Path) obj).names);
    }

    @Override
    public String toString() {
        return names.stream()
                .map(Name::toString)
                .collect(Collectors.joining("."));
    }
}
