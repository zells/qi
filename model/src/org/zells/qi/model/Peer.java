package org.zells.qi.model;

import org.zells.qi.model.deliver.Delivery;

public interface Peer {

    boolean deliver(Delivery delivery);
}
