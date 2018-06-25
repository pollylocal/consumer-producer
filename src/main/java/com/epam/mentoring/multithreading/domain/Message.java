package com.epam.mentoring.multithreading.domain;

/**
 * @author Aliaksandr Makavetski(UC215698)
 */
public class Message {
    private final Long order;
    private final String text;

    public Message(Long order, String text) {
        this.order = order;
        this.text = text;
    }

    public Long getOrder() {
        return order;
    }

    public String getText() {
        return text;
    }
}
