package com.epam.mentoring.multithreading.producer;

import com.epam.mentoring.multithreading.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Aliaksandr Makavetski(UC215698)
 */
public class Producer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final Generator generator;
    private final int order;
    private final int delay;
    private final BlockingQueue<Long> queue;

    public Producer(Generator generator, int order, int delay, BlockingQueue<Long> queue) {
        this.generator = generator;
        this.order = order;
        this.delay = delay;
        this.queue = queue;
    }

    public void run() {
        logger.info("Producer-{} started with delay {}", order, delay);
        while (!Thread.currentThread().isInterrupted()) {
            Long number = generator.getNextNumber();
            logger.info("Producer-{} got number {}", order, number);
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
                logger.info("Producer-{} is trying to add {} to queue", order, number);
                queue.put(number);
                logger.info("Producer-{} added {} to queue", order, number);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        logger.info("Producer-{} was interrupted and finished data processing", order);
    }
}
