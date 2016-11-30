package org.zells.qi;

class Frame extends Name {

    private Frame() {
    }

    static Name name() {
        return new Frame();
    }

    @Override
    public String toString() {
        return "#";
    }
}
