package com.contoso;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Target({ReentrantLock.class, Condition.class})
public class ReentrantLockTest {

    void foo(ReentrantLock lock) {
        lock.lock();
        try {
            bar(lock);
        } finally {
            lock.unlock();
        }
    }

    void bar(ReentrantLock lock) {
        lock.lock();
        try {
            Assert.assertTrue(lock.isLocked());
            Assert.assertTrue(lock.isHeldByCurrentThread());
            Assert.assertEquals(lock.getHoldCount(), 2);
        } finally {
            lock.unlock();
        }
        Assert.assertTrue(lock.isLocked());
        Assert.assertTrue(lock.isHeldByCurrentThread());
        Assert.assertEquals(lock.getHoldCount(), 1);
    }

    synchronized void a() {
        b();
    }

    synchronized void b() {
        System.out.println("OK");
    }

    @Test
    public void reentrant() {
        ReentrantLock lock = new ReentrantLock(false /* non-fair */);
        //synchronized (this) {
        //    // The choice is arbitrary and occurs at the discretion of the implementation.
        //    notify();
        //}
        foo(lock);
        a();
    }

    @Test
    public void lockInterruptibly() {
        Thread mainThread = Thread.currentThread();
        ReentrantLock lock = new ReentrantLock();
        Thread t = new Thread(lock::lock);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            //
        }
        new Thread(() -> {
            Utils.sleep(500);
            mainThread.interrupt();
        }).start();
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        Assert.assertTrue(lock.isLocked());
        Assert.assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void lock() {
        Thread mainThread = Thread.currentThread();
        ReentrantLock lock = new ReentrantLock();
        Thread t = new Thread(lock::lock);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            //
        }
        new Thread(() -> {
            Utils.sleep(500);
            mainThread.interrupt();
        }).start();
        lock.lock();
        Assert.fail();
    }

    @Test
    public void tryLock() {
        new ReentrantLock().tryLock();
        try {
            new ReentrantLock().tryLock(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }

    @Test
    public void condition() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock(); // Otherwise an IllegalMonitorStateException will be thrown
        try {
            Condition cond = lock.newCondition();
            Condition anotherCond = lock.newCondition();
            cond.awaitUninterruptibly();
            try {
                cond.await(); // -> Object.wait()
                cond.await(1, TimeUnit.MINUTES); // -> Object.wait()
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            cond.signal(); // Object.notify()
            cond.signalAll(); // Object.notifyAll()
        } finally {
            lock.unlock();
        }
    }
}
