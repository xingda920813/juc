package com.contoso;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.*;

@SuppressWarnings("unused")
interface Intro {

    Class<?>[] $ = {
            // Atomic
            AtomicInteger.class,
            AtomicIntegerFieldUpdater.class,
            LongAdder.class,

            // Locks
            ReentrantLock.class,
            Condition.class,
            ReentrantReadWriteLock.class,
            LockSupport.class,
            StampedLock.class,
            CountDownLatch.class,
            CyclicBarrier.class,
            Semaphore.class,

            // Blocking Queues
            ArrayBlockingQueue.class,
            LinkedTransferQueue.class,
            PriorityBlockingQueue.class,
            SynchronousQueue.class,

            // Concurrent Collections
            ConcurrentHashMap.class,
            ConcurrentLinkedQueue.class,
            ConcurrentSkipListMap.class,
            CopyOnWriteArrayList.class,

            // Executors
            Executors.class,
            ForkJoinPool.class,

            // Future
            CompletableFuture.class,
            ForkJoinTask.class
    };
}
