package com.epam.mentoring.multithreading;

import com.epam.mentoring.multithreading.consumer.Consumer;
import com.epam.mentoring.multithreading.domain.Message;
import com.epam.mentoring.multithreading.filewriter.OrderedFileWriter;
import com.epam.mentoring.multithreading.generator.AtomicLongGenerator;
import com.epam.mentoring.multithreading.generator.Generator;
import com.epam.mentoring.multithreading.producer.Producer;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author Aliaksandr Makavetski(UC215698)
 */
public class Application {

    public static void main(String[] args) {
        Generator generator = new AtomicLongGenerator();
        ExecutorService executorService = Executors.newCachedThreadPool();
        BlockingQueue<Long> queue = new ArrayBlockingQueue<>(10);
        BlockingQueue<Message> writingQueue = new PriorityBlockingQueue<>(10, Comparator.comparingLong(Message::getOrder));
        Random random = new Random();
        for (int i = 1; i <= 20; i++){
            executorService.submit(new Producer(generator, i, 2000 + random.nextInt(4000), queue));
        }
        for (int i = 1; i <= 20; i++){
            executorService.submit(new Consumer(i, 2000 + random.nextInt(4000), queue, writingQueue));
        }
        executorService.submit(new OrderedFileWriter("result.txt", writingQueue));
    }
}
