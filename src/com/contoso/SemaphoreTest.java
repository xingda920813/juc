package com.contoso;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Target(Semaphore.class)
public class SemaphoreTest {

    AtomicInteger mConcurrency = new AtomicInteger(0);
    AtomicInteger mExecutionTimes = new AtomicInteger(0);

    void workWithMax5Concurrency() {
        if (mConcurrency.incrementAndGet() > 5) {
            Assert.fail();
        }
        // do the job
        // ....
        mExecutionTimes.incrementAndGet();
        // ...
        mConcurrency.decrementAndGet();
    }

    @Test
    public void foo() {
        Semaphore semaphore = new Semaphore(5);
        Utils.runIn8ThreadsAndWaitUntilDone(threadNo -> {
            for (int unused = 0; unused < 10000; unused++) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    //
                }
                try {
                    workWithMax5Concurrency();
                } finally {
                    semaphore.release();
                }
            }
        });
        System.out.println("Executed " + mExecutionTimes.get() + " times");
    }
}
