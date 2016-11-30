package org.zells.qi;

class Message extends Name {

    private Message() {
    }

    @Override
    public String toString() {
        return "@";
    }

    public static Name name() {
        return new Message();
    }
}
