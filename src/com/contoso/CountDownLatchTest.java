package com.contoso;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@Target(CountDownLatch.class)
public class CountDownLatchTest {

    static class FileManager {

        static void readFileAsync(String path, Consumer<String> callback) {
            new Thread(() -> {
                // Simulate time-consuming operations
                Utils.sleep(800);
                callback.accept(path + ": Hello, World!");
            }).start();
        }
    }

    String readFileSync(String path) {
        String[] value = {null};
        CountDownLatch latch = new CountDownLatch(1);
        FileManager.readFileAsync(path, s -> {
            try {
                value[0] = s;
            } finally {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            //
        }
        return value[0];
    }

    @Test
    public void asyncToSync() {
        System.out.println(readFileSync("/a.txt"));
    }

    @Test
    public void zeroInitialCount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(0);
        latch.await();
    }

    @Test
    public void doubleInitialCount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        latch.countDown();
        latch.countDown();
        latch.await();
    }
}
