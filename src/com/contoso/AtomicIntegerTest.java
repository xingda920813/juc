package com.contoso;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@Target({AtomicInteger.class, AtomicIntegerFieldUpdater.class})
public class AtomicIntegerTest {

    @Test
    public void rawInt() {
        long start = System.currentTimeMillis();
        int[] value = {0};
        Utils.runIn8ThreadsAndWaitUntilDone(() -> {
            for (int unused = 0; unused < 1000000; unused++) {
                value[0]++;
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("cost = " + (end - start));
        System.out.println(value[0]);
    }

    @Test
    public void getAndSet_WrongUsage() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Utils.runIn8ThreadsAndWaitUntilDone(() -> {
            for (int unused = 0; unused < 1000000; unused++) {
                int i = atomicInteger.get();
                i = i + 1;
                atomicInteger.set(i);
            }
        });
        System.out.println(atomicInteger.get());
    }

    @Test
    public void compareAndSet() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        AtomicInteger races = new AtomicInteger(0);
        Utils.runIn8ThreadsAndWaitUntilDone(() -> {
            for (int unused = 0; unused < 1000000; unused++) {
                int i = atomicInteger.get();
                if (atomicInteger.compareAndSet(i, i + 1)) {
                    // Updated successfully
                } else {
                    int nowValue = atomicInteger.get();
                    if (races.getAndIncrement() < 5) {
                        System.out.println("Race condition, now the value has become " + nowValue + ", while the original value is " + i);
                    }
                }
            }
        });
        System.out.println("races = " + races.get());
    }

    @Test
    public void addAndGet_GetAndAdd() {
        long start = System.currentTimeMillis();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Utils.runIn8ThreadsAndWaitUntilDone(() -> {
            for (int unused = 0; unused < 1000000; unused++) {
                atomicInteger.addAndGet(1);

                //atomicInteger.getAndAdd(1);

                //atomicInteger.incrementAndGet();
                //atomicInteger.getAndIncrement();

                //atomicInteger.decrementAndGet();
                //atomicInteger.getAndDecrement();

                //atomicInteger.getAndSet(1024);
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("cost = " + (end - start));
        System.out.println(atomicInteger.get());
    }
}
