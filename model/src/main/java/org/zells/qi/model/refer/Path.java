package org.zells.qi.model.refer;

import org.zells.qi.model.refer.names.Parent;
import org.zells.qi.model.refer.names.Root;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Path {

    private final List<Name> names;

    public Path(List<Name> names) {
        this.names = names;
    }

    public Path(Name... names) {
        this.names = Arrays.asList(names);
    }

    public Name last() {
        return names.get(names.size() - 1);
    }

    public Name first() {
        return names.get(0);
    }

    public Path rest() {
        return new Path(names.subList(1, names.size()));
    }

    public Path with(Path path) {
        Path newPath = this;
        for (Name n : path.names) {
            newPath = newPath.with(n);
        }
        return newPath;
    }

    public Path with(Name name) {
        if (name.equals(Root.name())) {
            return new Path(name);
        }
        if (!isEmpty() && name.equals(Parent.name()) && !last().equals(Parent.name())) {
            return up();
        }

        List<Name> newNames = new ArrayList<>(names.size() + 1);
        for (Name n : names) {
            newNames.add(n);
        }
        newNames.add(name);

        return new Path(newNames);
    }

    public Path up() {
        return new Path(names.subList(0, names.size() - 1));
    }

    public boolean isEmpty() {
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
