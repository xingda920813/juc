package com.contoso;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Target(CyclicBarrier.class)
public class CyclicBarrierTest {

    @Test
    public void foo() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(8);
        Utils.runIn8ThreadsAndWaitUntilDone(threadNo -> {
            Utils.sleep(new Random().nextInt(2000));
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            long time = System.currentTimeMillis();
            System.out.println("Current time: " + time);
        });
        // reset
    }
}
