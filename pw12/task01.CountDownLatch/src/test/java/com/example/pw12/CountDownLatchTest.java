package com.example.pw12;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @RepeatedTest(10)
    @Test
    void testBasicsOneThread() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            assertEquals(5 + i, latch.getCount());
            latch.countUp();
        }

        for (int i = 0; i < 10; i++) {
            assertEquals(Math.max(0, 10 - i), latch.getCount());
            assertDoesNotThrow(latch::countDown);
        }
    }

    @RepeatedTest(10)
    @Test
    void testBasicAwait() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        Thread threads[] = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }
        latch.await();
        assertEquals(0, latch.getCount());
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
