package com.java.util.concurrent;

import java.util.concurrent.*;

public class FutureTest {

    static class AsyncClass implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("开始异步执行业务操作");
            Thread.sleep(1000);
            return 1;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        Future<Integer> future = Executors.newCachedThreadPool().submit(new AsyncClass());
        System.out.println(future.isDone());
        System.out.println(future.get());
//        System.out.println(future.get(500, TimeUnit.MILLISECONDS));
        System.out.println(future.isDone());
    }
}
