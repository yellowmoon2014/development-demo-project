package com.java.lang.ref;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class WeakReferenceTest {

    public static void main(String[] args) throws InterruptedException {
        Reference<Integer> reference = new WeakReference(new String("hy"));
        System.out.println(reference.get());
        System.gc();
        Thread.sleep(5000);
        System.out.println(reference.get());
    }

}
