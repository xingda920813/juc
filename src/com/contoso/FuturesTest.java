package com.contoso;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@Target({CompletableFuture.class, ForkJoinTask.class})
public class FuturesTest {

    @Test
    public void completableFuture() {

    }

    public static class CustomRecursiveAction extends RecursiveAction {

        private final int lo, hi;
        private static final int THRESHOLD = 4;

        public CustomRecursiveAction(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if (hi - lo < 0) return;
            if (hi - lo + 1 > THRESHOLD) {
                ForkJoinTask.invokeAll(createSubtasks());
            } else {
                processing();
            }
        }

        private List<CustomRecursiveAction> createSubtasks() {
            int mid = (lo + hi) / 2;
            return Arrays.asList(
                    new CustomRecursiveAction(lo, mid),
                    new CustomRecursiveAction(mid + 1, hi));
        }

        private void processing() {
            Utils.sleep(hi - lo + 1);
            System.out.println("This result - (" + lo + " - " + hi + ") - was processed by " + Thread.currentThread().getName());
        }
    }

    @Test
    public void singleThread() {
        long start = System.currentTimeMillis();

        int len = 4000;
        Utils.sleep(len);

        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void forkJoinTask() {
        long start = System.currentTimeMillis();

        int len = 4000;
        new ForkJoinPool().invoke(new CustomRecursiveAction(0, len - 1));

        System.out.println(System.currentTimeMillis() - start);
    }
}
