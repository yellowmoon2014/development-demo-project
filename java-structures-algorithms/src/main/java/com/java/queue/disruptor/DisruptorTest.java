package com.java.queue.disruptor;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

/**
 * -vm: -Xmx3m -Xms2m -XX:+HeapDumpOnOutOfMemoryError
 */
public class DisruptorTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadFactory threadFactory = r -> new Thread(r);
        Disruptor<Map<String, Object>> disruptor = new Disruptor<>(() -> new HashMap<>(), 1024*1, threadFactory);
        disruptor.handleEventsWith((e, s ,l)-> System.out.println(e));
        disruptor.start();

        RingBuffer<Map<String, Object>> ringBuffer = disruptor.getRingBuffer();
//        EventTranslator[] translators = new EventTranslator[1024*1];
        for (int i = 0; i < 1024; i++) {
            EventTranslator<Map<String, Object>> eventTranslator = (e, s) -> e.put("count", new byte[1024*2]);
//            translators[i] = eventTranslator;
            ringBuffer.publishEvent(eventTranslator);
        }
//        ringBuffer.publishEvents(translators);
    }
}
