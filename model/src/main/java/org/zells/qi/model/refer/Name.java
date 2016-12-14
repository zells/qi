package org.zells.qi.model.refer;

public abstract class Name {

    @Override
    abstract public String toString();

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && obj.toString().equals(toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
