package com.epam.mentoring.multithreading.filewriter;

import com.epam.mentoring.multithreading.consumer.Consumer;
import com.epam.mentoring.multithreading.domain.Message;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

/**
 * @author Aliaksandr Makavetski(UC215698)
 */
public class OrderedFileWriter implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(OrderedFileWriter.class);
    private final File file;
    private final BlockingQueue<Message> writingQueue;
    private int counter = 0;

    public OrderedFileWriter(String fileName, BlockingQueue<Message> writingQueue) {
        file = new File(fileName);
        this.writingQueue = writingQueue;
    }

    @Override
    public void run() {
        try {
            FileUtils.write(file, "", StandardCharsets.UTF_8);
            while (!Thread.currentThread().isInterrupted()){
                writeToFile();
            }
            logger.info("File writer was interrupted and finished data processing");
        } catch (IOException e) {
            throw new RuntimeException("An exception occurred while writing to file", e);
        }
    }

    private void writeToFile() throws IOException {
        try {
            Message currentMessage = writingQueue.take();
            if(currentMessage.getOrder() == counter + 1)
            {
                FileUtils.write(file, currentMessage.getText(), StandardCharsets.UTF_8, true);
                FileUtils.write(file, "\n", StandardCharsets.UTF_8, true);
                counter++;
            } else {
                writingQueue.put(currentMessage);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
