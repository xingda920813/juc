package com.contoso;

import java.util.concurrent.locks.StampedLock;

@Target(StampedLock.class)
public class StampedLockTest {

    public static class Point {

        private final StampedLock mStampedLock = new StampedLock();

        private double x;
        private double y;

        public void move(double deltaX, double deltaY) {
            long stamp = mStampedLock.writeLock();
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                mStampedLock.unlockWrite(stamp);
            }
        }

        public double distanceFromOrigin() {
            long stamp = mStampedLock.tryOptimisticRead(); // Optimistic Read
            // Note that the following two lines of code are not atomic operations
            // Assume x,y = (100,200)
            double currentX = x;
            // x is read as 100，but (x,y) may already be modified to (300,400)
            double currentY = y;
            // y has been read, if there's not concurrent writing, (100,200) will be read correctly;
            // If there's concurrent writing, the read operation was wrong.
            if (!mStampedLock.validate(stamp)) { // Check if any write lock is acquired after the stamp
                stamp = mStampedLock.readLock(); // Pessimistic Lock
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    mStampedLock.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }

        public void moveIfAtOrigin(double newX, double newY) {
            long stamp = mStampedLock.readLock();
            try {
                while (x == 0.0 && y == 0.0) {
                    long ws = mStampedLock.tryConvertToWriteLock(stamp);
                    if (ws != 0L) {
                        stamp = ws;
                        x = newX;
                        y = newY;
                        break;
                    } else {
                        mStampedLock.unlockRead(stamp);
                        stamp = mStampedLock.writeLock();
                    }
                }
            } finally {
                mStampedLock.unlock(stamp);
            }
        }
    }
}
