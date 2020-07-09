package com.contoso.executor;

import com.contoso.Target;
import com.contoso.Utils;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

@Target({Executor.class,
        ExecutorService.class,
        ScheduledExecutorService.class,
        CompletionService.class,
        ExecutorCompletionService.class,
        Executors.class,
        ThreadPoolExecutor.class})
public class ExecutorsTest {

    @Test
    public void taskPostedBeforeShutdown() {
        ThreadPoolExecutor executor = Utils.newSingleThreadExecutor();
        executor.execute(() -> {
            Utils.sleep(1000);
            System.out.println("Task posted before shutdown, but run after shutdown");
        });
        executor.shutdown();
        LockSupport.park();
    }

    @Test
    public void taskPostedAfterShutdown() {
        ThreadPoolExecutor executor = Utils.newSingleThreadExecutor();
        executor.shutdown();
        executor.execute(() -> System.out.println("Task posted after shutdown"));
    }

    @Test
    public void threadPoolExecutor_rejectExecutionHandler() {
        ThreadPoolExecutor executor = Utils.newSingleThreadExecutor();
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // Also the default
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
