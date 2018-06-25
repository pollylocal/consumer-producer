package com.epam.mentoring.multithreading.consumer;

import com.epam.mentoring.multithreading.domain.Message;
import com.epam.mentoring.multithreading.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Aliaksandr Makavetski(UC215698)
 */
public class Consumer implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);


    private final int order;
    private final int delay;
    private final BlockingQueue<Long> queue;
    private final BlockingQueue<Message> writingQueue;

    public Consumer(int order, int delay, BlockingQueue<Long> queue, BlockingQueue<Message> writingQueue) {
        this.order = order;
        this.delay = delay;
        this.queue = queue;
        this.writingQueue = writingQueue;
    }

    public void run() {
        logger.info("Consumer-{} started with delay {}", order, delay);
        while (!Thread.currentThread().isInterrupted()){
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
                logger.info("Consumer-{} is trying to get value from queue", order);
                Long number = queue.take();
                logger.info("Consumer-{} got value {}", order, number);
                writingQueue.add(new Message(number, number + " was processed"));
                logger.info("Consumer-{} added {} to queue", order, number);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
