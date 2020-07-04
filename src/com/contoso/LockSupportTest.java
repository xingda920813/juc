package com.contoso;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

@Target(LockSupport.class)
public class LockSupportTest {

    final String mLock = "Lock";

    @Test
    public void foo() {
        Thread mainThread = Thread.currentThread();
        new Thread(() -> {
            Utils.sleep(200);
            System.out.println(LockSupport.getBlocker(mainThread));
            Utils.sleep(1200);
            LockSupport.unpark(mainThread);
        }).start();
        LockSupport.park(mLock); // or parkNanos(), parkUntil()
        System.out.println("I'm happy to run again");
    }
}
