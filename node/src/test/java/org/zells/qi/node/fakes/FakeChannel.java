package org.zells.qi.node.fakes;

import org.zells.qi.node.connecting.Channel;
import org.zells.qi.node.singalling.Signal;
import org.zells.qi.node.singalling.signals.ReceivedSignal;

import java.util.HashMap;
import java.util.Map;

public class FakeChannel implements Channel {

    public Signal sent;

    public static Map<String, FakeChannel> channels = new HashMap<>();

    public FakeChannel(String connection) {
        channels.put(connection, this);
    }

    @Override
    public Signal send(Signal signal) {
        sent = signal;
        return new ReceivedSignal();
    }

    public static void reset() {
        channels.clear();
    }
}
