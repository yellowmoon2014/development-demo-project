package com.java.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class QueueTest {

    private static final Broker broker = new Broker();

    public static void main(String[] args) {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

    static class Producer implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                broker.send(String.valueOf(i));
            }
        }
    }


    static class Consumer implements Runnable {

        @Override
        public void run() {
            broker.subscribe(e -> System.out.println(e));
        }
    }

    static class Broker {

        private BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>(10);

        public void send(String message) {
            try {
                blockingQueue.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void subscribe(java.util.function.Consumer<String> consumer) {
            while (true) {
                try {
                    if (blockingQueue.isEmpty()) {
                        Thread.sleep(5000);
                    }
                    consumer.accept(blockingQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
