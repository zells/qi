package org.zells.qi.model.refer.names;

import org.zells.qi.model.refer.Name;

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
