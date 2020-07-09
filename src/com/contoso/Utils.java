package com.contoso;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.function.IntConsumer;

public interface Utils {

    static void runIn8ThreadsAndWaitUntilDone(Runnable r) {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int threadNo = 0; threadNo < 8; threadNo++) {
            Thread t = new Thread(r);
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void runIn8ThreadsAndWaitUntilDone(IntConsumer r) {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int threadNo = i;
            Thread t = new Thread(() -> r.accept(threadNo));
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static ThreadPoolExecutor newSingleThreadExecutor() {
        return new ThreadPoolExecutor(1, 1,
                0, TimeUnit.MILLISECONDS,
                new LinkedTransferQueue<>());
    }
}
