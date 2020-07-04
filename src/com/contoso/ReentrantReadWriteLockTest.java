package com.contoso;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Target(ReentrantReadWriteLock.class)
public class ReentrantReadWriteLockTest {

    AtomicInteger mCount = new AtomicInteger();
    ArrayList<Integer> mList = new ArrayList<>();
    ReentrantReadWriteLock mReadWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock mReadLock = mReadWriteLock.readLock();
    ReentrantReadWriteLock.WriteLock mWriteLock = mReadWriteLock.writeLock();

    void write() {
        mWriteLock.lock();
        try {
            mList.add(mCount.incrementAndGet());
        } finally {
            mWriteLock.unlock();
        }
    }

    int read(int threadNo) {
        Utils.sleep(100 * threadNo);
        Random r = ThreadLocalRandom.current();
        mReadLock.lock();
        try {
            if (threadNo != 7) {
                Utils.sleep(Integer.MAX_VALUE);
            }
            return mList.get(r.nextInt(mList.size()));
        } finally {
            mReadLock.unlock();
        }
    }

    @Test
    public void readOnly() {
        for (int i = 0; i < 100; i++) {
            write();
        }
        Utils.runIn8ThreadsAndWaitUntilDone(threadNo -> System.out.println(read(threadNo)));
    }









    int read() {
        Random r = ThreadLocalRandom.current();
        mReadLock.lock();
        try {
            return mList.get(r.nextInt(mList.size()));
        } finally {
            mReadLock.unlock();
        }
    }

    ReentrantLock mReentrantLock = new ReentrantLock();

    void slowWrite() {
        mReentrantLock.lock();
        try {
            mList.add(mCount.incrementAndGet());
        } finally {
            mReentrantLock.unlock();
        }
    }

    int slowRead() {
        Random r = ThreadLocalRandom.current();
        mReentrantLock.lock();
        try {
            return mList.get(r.nextInt(mList.size()));
        } finally {
            mReentrantLock.unlock();
        }
    }

    @Test
    public void compareTheSpeed() {
        for (int i = 0; i < 100; i++) {
            write();
        }

        long slowStart = System.currentTimeMillis();
        Utils.runIn8ThreadsAndWaitUntilDone(threadNo -> {
            if (threadNo <= 0) {
                // write
                for (int unused = 0; unused < 10000; unused++) {
                    slowWrite();
                }
            } else {
                // read
                for (int unused = 0; unused < 10000; unused++) {
                    slowRead();
                }
            }
        });
        long slowEnd = System.currentTimeMillis();
        System.out.println("slow cost = " + (slowEnd - slowStart));

        mList.clear();
        mList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            write();
        }

        long fastStart = System.currentTimeMillis();
        Utils.runIn8ThreadsAndWaitUntilDone(threadNo -> {
            if (threadNo <= 0) {
                // write
                for (int unused = 0; unused < 10000; unused++) {
                    write();
                }
            } else {
                // read
                for (int unused = 0; unused < 10000; unused++) {
                    read();
                }
            }
        });
        long fastEnd = System.currentTimeMillis();
        System.out.println("fast cost = " + (fastEnd - fastStart));
    }
}
