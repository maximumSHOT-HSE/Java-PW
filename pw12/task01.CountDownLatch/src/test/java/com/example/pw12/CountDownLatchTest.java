package com.example.pw12;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
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

    @RepeatedTest(10)
    public void testInit() {
    }

    @RepeatedTest(10)
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

    @RepeatedTest(10)
    void testCountDownLock() {
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
    void testBasicAwait() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        Thread[] threads = new Thread[10];
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
        assertDoesNotThrow(() -> latch.countDown());
        assertDoesNotThrow(() -> Thread.sleep(100));
        assertDoesNotThrow(() -> latch.countUp());
    }

    @RepeatedTest(10)
    void testNegativeConstructorArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                latch = new CountDownLatch(-10));
    }

    @RepeatedTest(10)
    void testCountUpLocking() throws InterruptedException {
        latch = new CountDownLatch(0);

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.countDown();
                    } catch (InterruptedException e) {
                        fail();
                    }
                }
            });
            threads[i].start();
        }

        Thread incrementer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        fail();
                    }
                    int ok = 1;
                    for (int i = 0; i < 10; i++) {
                        if (!threads[i].getState().equals(Thread.State.WAITING)) {
                            System.out.println("found = " + threads[i].getState());
                            ok = 0;
                            break;
                        }
                    }
                    if (ok == 1) {
                        break;
                    }
                }
                for (int i = 0; i < 10; i++) {
                    assertDoesNotThrow(() -> latch.countUp());
                }
            }
        });

        incrementer.start();

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }
    }
}
