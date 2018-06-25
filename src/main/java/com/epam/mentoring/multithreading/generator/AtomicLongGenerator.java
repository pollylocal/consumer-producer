package com.epam.mentoring.multithreading.generator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Aliaksandr Makavetski(UC215698)
 */
public class AtomicLongGenerator implements Generator {
    private final AtomicLong atomicLong = new AtomicLong(1);

    public Long getNextNumber() {
        return atomicLong.getAndIncrement();
    }
}
