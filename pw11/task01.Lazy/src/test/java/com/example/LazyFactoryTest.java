package com.example;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class LazyFactoryTest {

    @Test
    void createOneThreadLazyOneResultRandom() {
        Random random = new Random(179);
        Lazy<Integer> lazy = LazyFactory.createOneThreadLazy(random::nextInt);
        Integer first = lazy.get();
        for (int i = 0; i < 179; ++i) {
            assertEquals(first, lazy.get());
        }
    }

    @Test
    void createOneThreadLazyReturnsNull() {
        Lazy<String> lazy = LazyFactory.createOneThreadLazy(() -> null);
        assertNull(lazy.get());
    }

    @Test
    void createOneThreadLazyOneCall() {
        Lazy<String> lazy = LazyFactory.createOneThreadLazy(new ExplodingSupplier());
        assertDoesNotThrow(lazy::get);
        for (int i = 0; i < 179; ++i) {
            assertDoesNotThrow(lazy::get);
        }
    }

    @Test
    void createOneThreadLazyOneCallNull() {
        Lazy<String> lazy = LazyFactory.createOneThreadLazy(new ExplodingNullSupplier());
        assertDoesNotThrow(lazy::get);
        for (int i = 0; i < 179; ++i) {
            assertDoesNotThrow(lazy::get);
        }
    }

    @Test
    void createOneThreadLazyAdder() {
        Lazy<Integer> lazy = LazyFactory.createOneThreadLazy(new AddSupplier());
        Integer first = lazy.get();
        for (int i = 0; i < 179; ++i) {
            assertEquals(first, lazy.get());
        }
    }

    @RepeatedTest(10)
    void createMultiThreadLazyRandom() throws InterruptedException {
        Random random = new Random(179);
        Lazy<Integer> lazy = LazyFactory.createMultiThreadLazy(random::nextInt);
        int threadAmount = 20;
        Thread[] threads = new Thread[threadAmount];
        Integer[] result = new Integer[threadAmount];
        for (int i = 0; i < threadAmount; i++) {
            final int ii = i;
            threads[i] = new Thread(() -> result[ii] = lazy.get());
            threads[i].start();
        }
        for (int i = 0; i < threadAmount; ++i) {
            threads[i].join();
            assertEquals(result[0], result[i]);
        }
    }

    @RepeatedTest(10)
    void createMultiThreadLazyReturnsNull() {
        Lazy<String> lazy = LazyFactory.createMultiThreadLazy(() -> null);
        assertNull(lazy.get());
    }

    @RepeatedTest(10)
    void createLocklessMultiThreadLazyOneResultRandom() throws InterruptedException {
        Random random = new Random(179);
        Lazy<Integer> lazy = LazyFactory.createLocklessMultiThreadLazy(random::nextInt);
        int threadAmount = 20;
        Thread[] threads = new Thread[threadAmount];
        Integer[] result = new Integer[threadAmount];
        for (int i = 0; i < threadAmount; i++) {
            final int ii = i;
            threads[i] = new Thread(() -> result[ii] = lazy.get());
            threads[i].start();
        }
        for (int i = 0; i < threadAmount; ++i) {
            threads[i].join();
            assertEquals(result[0], result[i]);
        }
    }

    @RepeatedTest(10)
    void createLocklessMultiThreadedLazyAdder() throws InterruptedException {
        Lazy<Integer> lazy = LazyFactory.createLocklessMultiThreadLazy(new AddSupplier());
        int threadAmount = 20;
        Thread[] threads = new Thread[threadAmount];
        Integer[] result = new Integer[threadAmount];
        for (int i = 0; i < threadAmount; i++) {
            final int ii = i;
            threads[i] = new Thread(() -> result[ii] = lazy.get());
            threads[i].start();
        }
        for (int i = 0; i < threadAmount; ++i) {
            threads[i].join();
            assertEquals(result[0], result[i]);
        }
    }



    @RepeatedTest(10)
    void createMultiThreadAdder() throws InterruptedException {
        Lazy<Integer> lazy = LazyFactory.createMultiThreadLazy(new AddSupplier());
        int threadAmount = 20;
        Thread[] threads = new Thread[threadAmount];
        Integer[] result = new Integer[threadAmount];
        for (int i = 0; i < threadAmount; i++) {
            final int ii = i;
            threads[i] = new Thread(() -> result[ii] = lazy.get());
            threads[i].start();
        }
        for (int i = 0; i < threadAmount; ++i) {
            threads[i].join();
            assertEquals(result[0], result[i]);
        }
    }

    @RepeatedTest(10)
    void createLocklessMultiThreadLazyReturnsNull() {
        Lazy<String> lazy = LazyFactory.createLocklessMultiThreadLazy(() -> null);
        assertNull(lazy.get());
    }

    @RepeatedTest(10)
    void createMultiThreadExploding() throws InterruptedException {
        Lazy<String> lazy = LazyFactory.createMultiThreadLazy(new ExplodingSupplier());
        final IllegalStateException[] exception = {null};
        int threadAmount = 20;
        Thread[] threads = new Thread[threadAmount];
        String[] result = new String[threadAmount];
        for (int i = 0; i < threadAmount; i++) {
            final int ii = i;
            threads[i] = new Thread(() -> {
                try {
                    result[ii] = lazy.get();
                } catch (IllegalStateException e) {
                    exception[0] = e;
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < threadAmount; ++i) {
            threads[i].join();
            assertEquals(result[0], result[i]);
        }
        assertNull(exception[0]);
    }

    @RepeatedTest(10)
    void createMultiThreadExplodingNull() throws InterruptedException {
        Lazy<String> lazy = LazyFactory.createMultiThreadLazy(new ExplodingNullSupplier());
        final IllegalStateException[] exception = {null};
        int threadAmount = 20;
        Thread[] threads = new Thread[threadAmount];
        String[] result = new String[threadAmount];
        for (int i = 0; i < threadAmount; i++) {
            final int ii = i;
            threads[i] = new Thread(() -> {
                try {
                    result[ii] = lazy.get();
                } catch (IllegalStateException e) {
                    exception[0] = e;
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < threadAmount; ++i) {
            threads[i].join();
            assertEquals(result[0], result[i]);
        }
        assertNull(exception[0]);
    }


    private class ExplodingSupplier implements Supplier<String> {
        private boolean wasCalled = false;
        @Override
        public String get() {
            synchronized (this) {
                if (!wasCalled) {
                    wasCalled = true;
                    return "Ok, first call!";
                }
                throw new IllegalStateException("Supplier has exploded");
            }
        }
    }

    private class ExplodingNullSupplier implements Supplier<String> {
        private boolean wasCalled = false;
        @Override
        public String get() {
            synchronized (this) {
                if (!wasCalled) {
                    wasCalled = true;
                    return null;
                }
                throw new IllegalStateException("Supplier has exploded");
            }
        }
    }

    private class AddSupplier implements Supplier<Integer> {
        private int last = 0;
        @Override
        public Integer get() {
            synchronized (this) {
                last++;
                return last;
            }
        }
    }
}