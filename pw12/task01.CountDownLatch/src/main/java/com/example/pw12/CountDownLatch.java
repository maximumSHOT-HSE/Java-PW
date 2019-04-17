package com.example.pw12;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownLatch {

    private Lock lock = new ReentrantLock();
    private Condition isZero = lock.newCondition();
    private Condition isPositive = lock.newCondition();
    private int count;

    public CountDownLatch(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count should be a non-negative value, but found count = " + count);
        }
        this.count = count;
    }

    public void await() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        lock.lock();
        try {
            while (count != 0) {
                isZero.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void countDown() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        lock.lock();
        try {
            while (count == 0) {
                isPositive.await();
            }
            count--;
            if (count == 0) {
                isZero.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void countUp() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        lock.lock();
        try {

        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }
}
