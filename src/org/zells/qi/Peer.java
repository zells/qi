package org.zells.qi;

import org.zells.qi.deliver.Delivery;

public interface Peer {

    boolean deliver(Delivery delivery);
}
