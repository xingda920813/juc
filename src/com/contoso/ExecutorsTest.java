package com.contoso;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@Target({Executors.class, ForkJoinPool.class})
public class ExecutorsTest {

    static final int NP = Runtime.getRuntime().availableProcessors();

    // Executors

    @Test
    public void executorApi() {
        Executors.newCachedThreadPool();
        Executors.newFixedThreadPool(NP);
        Executors.newScheduledThreadPool(NP);
        Executors.newSingleThreadExecutor();
        Executors.newSingleThreadScheduledExecutor();
        Executors.newWorkStealingPool();
        Executors.newWorkStealingPool(NP);
    }

    //     * @param corePoolSize the number of threads to keep in the pool, even
    //     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
    //     *
    //     * @param maximumPoolSize the maximum number of threads to allow in the
    //     *        pool
    //     *
    //     * @param keepAliveTime when the number of threads is greater than
    //     *        the core, this is the maximum time that excess idle threads
    //     *        will wait for new tasks before terminating.
    //     *
    //     * @param unit the time unit for the {@code keepAliveTime} argument
    //     *
    //     * @param workQueue the queue to use for holding tasks before they are
    //     *        executed.  This queue will hold only the {@code Runnable}
    //     *        tasks submitted by the {@code execute} method.
    //     */
    //    public ThreadPoolExecutor(int corePoolSize,
    //                              int maximumPoolSize,
    //                              long keepAliveTime,
    //                              TimeUnit unit,
    //                              BlockingQueue<Runnable> workQueue) {
    //        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
    //             Executors.defaultThreadFactory(), defaultHandler);
    //    }

    // HxCore executor

    @Test
    public void forkJoinPool_WorkStealing() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        //forkJoinPool.execute((ForkJoinTask<Integer>) null);
        // A, B.
        // A has 2 slow tasks, B has 2 fast tasks.
        Runnable fastTask = () -> Utils.sleep(100);
        Runnable slowTask = () -> Utils.sleep(1000);
        for (int i = 0; i < 100; i++) {
            forkJoinPool.execute(slowTask);
            forkJoinPool.execute(fastTask);
        }
        Utils.sleep(8000);
        System.out.println(forkJoinPool.getStealCount());
    }
}
