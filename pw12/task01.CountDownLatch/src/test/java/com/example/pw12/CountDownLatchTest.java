package com.example.pw12;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountDownLatchTest {
    private CountDownLatch latch;
    private final int baseCntSize = 10;

    @BeforeEach
    public void init() {
        latch = new CountDownLatch(baseCntSize);
    }

    @Test
    public void testInit() {
    }

    @Test
    public void testSimpleAwait() {
        Thread actor = new Thread(this::actorForSimpleAwait);
        actor.start();
        latch.await();
    }

    private void actorForSimpleAwait() {
        while (latch.getCount() != 0) {
            latch.countDown();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
