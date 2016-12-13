package org.zells.qi.node.fakes;

import org.zells.qi.node.connecting.Channel;
import org.zells.qi.node.connecting.Signal;
import org.zells.qi.node.connecting.signals.OkSignal;

import java.util.HashMap;
import java.util.Map;

public class FakeChannel implements Channel {

    public String sent;

    public static Map<String, FakeChannel> channels = new HashMap<>();

    public FakeChannel(String connection) {
        channels.put(connection, this);
    }

    @Override
    public Signal send(Signal signal) {
        sent = signal.toString();
        return new OkSignal();
    }

    public static void reset() {
        channels.clear();
    }
}
