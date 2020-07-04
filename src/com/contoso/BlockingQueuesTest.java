package com.contoso;

import org.junit.Test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

@Target({ArrayBlockingQueue.class,
        LinkedBlockingDeque.class,
        PriorityBlockingQueue.class,
        LinkedTransferQueue.class,
        SynchronousQueue.class})
public class BlockingQueuesTest {

    // wash dishes model

    @Test
    public void linkedBlockingQueue() {
        LinkedBlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
        new Thread(() -> {
            for (;;) {
                //blockingQueue.poll(); // Non blocking
                //blockingQueue.remove(); // Non blocking
                try {
                    //blockingQueue.poll(1, TimeUnit.MINUTES);
                    Integer value = blockingQueue.take();
                    System.out.println(value);
                } catch (InterruptedException e) {
                    //
                }
            }
        }, "read").start();
        new Thread(() -> {
            for (;;) {
                Utils.sleep(1000);
                //blockingQueue.add(1024); // Non blocking
                //blockingQueue.offer(1024); // Non blocking
                try {
                    blockingQueue.put(1024);
                    //blockingQueue.offer(1024, 1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    //
                }
            }
        }, "write").start();
        LockSupport.park();
    }

    @Test
    public void writingIsFaster() {
        LinkedBlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
        new Thread(() -> {
            Utils.sleep(2000);
            for (;;) {
                //blockingQueue.poll(); // Non blocking
                //blockingQueue.remove(); // Non blocking
                try {
                    //blockingQueue.poll(1, TimeUnit.MINUTES);
                    Integer value = blockingQueue.take();
                    System.out.println(value);
                } catch (InterruptedException e) {
                    //
                }
            }
        }, "read").start();
        new Thread(() -> {
            int count = 0;
            for (;;) {
                //blockingQueue.add(1024); // Non blocking
                //blockingQueue.offer(1024); // Non blocking
                try {
                    blockingQueue.put(1024);
                    //blockingQueue.offer(1024, 1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    //
                }
                if (++count >= 20) {
                    System.out.println("Writing is done.");
                    return;
                }
            }
        }, "write").start();
        LockSupport.park();
    }

    @Test
    public void arrayBlockingQueue() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        for (int i = 0; i < 4; i++) {
            System.out.println(blockingQueue.offer(1024));
        }
        blockingQueue.clear();
        for (int i = 0; i < 4; i++) {
            try {
                blockingQueue.put(1024);
                System.out.println("Put'ed");
            } catch (InterruptedException e) {
                //
            }
        }
    }

    @Test
    public void minHeap() {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        for (int i = 0; i < 10; i++) {
            maxHeap.offer(new Random().nextInt(2000));
        }
        while (!maxHeap.isEmpty()) {
            System.out.println(maxHeap.remove());
        }
    }

    @Test
    public void synchronousQueue() {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();
        new Thread(() -> {
            for (;;) {
                try {
                    System.out.println(synchronousQueue.take());
                } catch (InterruptedException e) {
                    //
                }
            }
        }, "read").start();
        new Thread(() -> {
            for (;;) {
                Utils.sleep(1000);
                try {
                    synchronousQueue.put(1024);
                } catch (InterruptedException e) {
                    //
                }
            }
        }, "write").start();
        LockSupport.park();
    }

    @Test
    public void synchronousQueue_WritingIsFaster() {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();
        new Thread(() -> {
            Utils.sleep(2000);
            for (;;) {
                try {
                    System.out.println(synchronousQueue.take());
                } catch (InterruptedException e) {
                    //
                }
            }
        }, "read").start();
        new Thread(() -> {
            int count = 0;
            for (;;) {
                try {
                    synchronousQueue.put(1024);
                } catch (InterruptedException e) {
                    //
                }
                if (++count >= 20) {
                    System.out.println("Writing is done.");
                    return;
                }
            }
        }, "write").start();
        LockSupport.park();
    }

    // HxCore executors

    @Test
    public void transferQueue() {
        TransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();
    }
}
