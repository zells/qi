package org.zells.qi.model;

import org.zells.qi.model.deliver.Delivery;

public interface Courier {

    boolean deliver(Delivery delivery);
}
