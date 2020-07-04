package com.contoso;

import org.junit.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

// diff with Collections, iterator, efficiency
@Target({
        ConcurrentHashMap.class, // why not generic concurrent arrayList?
        ConcurrentLinkedQueue.class,
        ConcurrentLinkedDeque.class,
        ConcurrentSkipListMap.class, // treeMap, time complexity
        ConcurrentSkipListSet.class,
        CopyOnWriteArrayList.class,
        CopyOnWriteArraySet.class
})
public class ConcurrentCollectionsTest {

    static class HashMapThread extends Thread {

        private static final AtomicInteger ai = new AtomicInteger(0);
        private static final Map<Integer, Integer> map = new HashMap<>(1);

        @Override
        public void run() {
            while (ai.get() < 100000) {
                map.get(ai.get());
                map.put(ai.get(), ai.get());
                ai.incrementAndGet();
            }
            System.out.println(Thread.currentThread().getName() + " execution done");
        }
    }

    @Test
    public void hashMap_infiniteLoop() {
        // ArrayList
        for (int i = 0; i < 5; i++) {
            new HashMapThread().start();
        }
        LockSupport.park();
    }

    @Test
    public void chm() {
        ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
        try {
            map.put(null, "Test");
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            map.put("Test", null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void navigableMap() {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(20, "Hello");
        treeMap.put(40, "Java");
        treeMap.put(80, "Scala");
        System.out.println(treeMap.lowerEntry(40));
        System.out.println(treeMap.higherEntry(40));
    }
}
