package com.example.pw12;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountDownLatchTest {

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
}