package org.zells.qi.model;

import org.zells.qi.model.deliver.Delivery;

public interface Reaction {

    void execute(Delivery delivery);
}
