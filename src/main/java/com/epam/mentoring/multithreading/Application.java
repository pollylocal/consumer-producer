package com.epam.mentoring.multithreading;

import com.epam.mentoring.multithreading.consumer.Consumer;
import com.epam.mentoring.multithreading.domain.Message;
import com.epam.mentoring.multithreading.filewriter.OrderedFileWriter;
import com.epam.mentoring.multithreading.generator.AtomicLongGenerator;
import com.epam.mentoring.multithreading.generator.Generator;
import com.epam.mentoring.multithreading.producer.Producer;
import com.epam.mentoring.multithreading.util.ConsoleReader;

import java.io.Console;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.*;

import static com.epam.mentoring.multithreading.util.ConsoleReader.readIntFromConsole;
import static com.epam.mentoring.multithreading.util.ConsoleReader.readStringFromConsole;

/**
 * @author Aliaksandr Makavetski(UC215698)
 */
public class Application {
    private static int producerCount = 5;
    private static int consumerCount = 5;
    private static int producerDelayFrom = 1000;
    private static int producerDelayTo = 5000;
    private static int consumerDelayFrom = 1000;
    private static int consumerDelayTo = 5000;

    public static void main(String[] args) {
        Generator generator = new AtomicLongGenerator();
        ExecutorService executorService = Executors.newCachedThreadPool();
        BlockingQueue<Long> queue = new ArrayBlockingQueue<>(10);
        BlockingQueue<Message> writingQueue = new PriorityBlockingQueue<>(10, Comparator.comparingLong(Message::getOrder));
        Random random = new Random();

        printDefaultSettings();
        String override = readStringFromConsole("Do you want to override default start up properties? (y) : ");
        if ("y".equals(override)) {
            producerCount = readIntFromConsole("Producer count: ");
            consumerCount = readIntFromConsole("Consumer count: ");
            producerDelayFrom = readIntFromConsole("Producer delay from: ");
            producerDelayTo = readIntFromConsole("Producer delay to: ");
            consumerDelayFrom = readIntFromConsole("Consumer delay from: ");
            consumerDelayTo = readIntFromConsole("Consumer delay to: ");
        }


        for (int i = 1; i <= producerCount; i++){
            int producerDelay = producerDelayFrom + random.nextInt(producerDelayTo - producerDelayFrom);
            executorService.submit(new Producer(generator, i, producerDelay, queue));
        }
        for (int i = 1; i <= consumerCount; i++){
            int consumerDalay = consumerDelayFrom + random.nextInt(consumerDelayTo - consumerDelayFrom);
            executorService.submit(new Consumer(i, consumerDalay, queue, writingQueue));
        }
        executorService.submit(new OrderedFileWriter("result.txt", writingQueue));

        readStringFromConsole("Press Enter to send interruption signal");
        executorService.shutdownNow();
    }

    private static void printDefaultSettings() {
        System.out.println("Default settings:");
        System.out.println(producerCount + " producers with delay from " + producerDelayFrom + " to " + producerDelayTo);
        System.out.println(consumerCount + " consumers with delay from " + consumerDelayFrom + " to " + consumerDelayTo);
    }
}
