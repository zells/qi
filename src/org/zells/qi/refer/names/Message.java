package org.zells.qi.refer.names;

import org.zells.qi.refer.Name;

public class Message extends Name {

    private Message() {
    }

    public static Name name() {
        return new Message();
    }

    @Override
    public String toString() {
        return "@";
    }
}
