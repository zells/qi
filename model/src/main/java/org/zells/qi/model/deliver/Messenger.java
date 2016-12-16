package org.zells.qi.model.deliver;

import org.zells.qi.model.Courier;

public class Messenger {
    private final Courier courier;
    private final Delivery delivery;
    private Runnable failed;
    private int maxRetries = 10;
    private boolean delivered = false;
    private boolean isDelivering = false;

    public Messenger(Courier courier, Delivery delivery) {
        this.courier = courier;
        this.delivery = delivery;
    }

    private void deliver() {
        delivered = courier.deliver(delivery);

        int retries = 0;
        while (!delivered && retries < maxRetries) {
            delivered = courier.deliver(delivery.renew());
            retries++;
            sleep(5 * retries);
        }

        if (!delivered && failed != null) {
            failed.run();
        }

        isDelivering = false;
    }

    public Messenger setMaxRetries(int max) {
        maxRetries = max;
        return this;
    }

    public Messenger whenFailed(Runnable runnable) {
        failed = runnable;
        return this;
    }

    public Messenger run() {
        isDelivering = true;
        (new Thread(this::deliver)).start();

        return this;
    }

    public Messenger waitForIt() {
        while (isDelivering) {
            sleep(10);
        }
        return this;
    }

    public boolean hasDelivered() {
        return delivered;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
