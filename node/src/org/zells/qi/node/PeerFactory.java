package org.zells.qi.node;

import org.zells.qi.model.Peer;

public interface PeerFactory {
    Peer buildFromConnection(String connection);
}
