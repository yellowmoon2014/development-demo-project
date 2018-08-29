package com.java.lang.ref;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class ReferenceQueueTest {
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<WeakReference<byte[]>> referenceQueue = new ReferenceQueue<>();
        Map<Reference<byte[]>, Object> map = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            byte[] bytes = new byte[1024*1024];
            Reference<byte[]> reference = new WeakReference(bytes, referenceQueue);
            map.put(reference, new Object());
        }
        System.out.println(map.size());
        new Thread(()-> {
            try {
                int cnt = 0;
                Reference<? extends WeakReference<byte[]>> k;
                while((k = referenceQueue.remove()) != null) {
                    System.out.println((cnt++) + "回收了:" + k);
                }
            } catch (Exception e) {

            }
        }).start();
        Thread.sleep(1000000);
    }
}
