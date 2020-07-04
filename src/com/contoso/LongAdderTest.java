package com.contoso;

import org.junit.Test;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

@Target({LongAdder.class, LongAccumulator.class})
public class LongAdderTest {

    @Test
    public void longAdder() {
        long start = System.currentTimeMillis();
        LongAdder longAdder = new LongAdder();
        Utils.runIn8ThreadsAndWaitUntilDone(() -> {
            for (int unused = 0; unused < 1000000; unused++) {
                longAdder.add(1);
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("cost = " + (end - start));
        System.out.println(longAdder.sum());
    }
}
