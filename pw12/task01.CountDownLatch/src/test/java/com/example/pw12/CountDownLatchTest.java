package com.example.pw12;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        assertDoesNotThrow(() -> latch.await());
    }

    private void actorForSimpleAwait() {
        while (latch.getCount() != 0) {
            assertDoesNotThrow(() -> latch.countDown());
            assertDoesNotThrow(() -> Thread.sleep(300));
        }
    }

    @Test
    public void testCountDownLock() {
        latch = new CountDownLatch(1);
        List<Thread> actors = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            actors.add(new Thread(this::actorForCountDownLock));
            actors.get(i).start();
        }
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> actors.get(finalI).join());
        }
    }

    private void actorForCountDownLock() {
//        System.out.println("Before CountDown at " + Thread.currentThread().getName());
        assertDoesNotThrow(() -> latch.countDown());
//        System.out.println("CountDown at " + Thread.currentThread().getName());
        assertDoesNotThrow(() -> Thread.sleep(200));
//        System.out.println("Slept at " + Thread.currentThread().getName());
        assertDoesNotThrow(() -> latch.countUp());
//        System.out.println("CountUp at " + Thread.currentThread().getName());
//        System.out.println("Latch count is " + latch.getCount() + " at " + Thread.currentThread().getName());
    }
}
