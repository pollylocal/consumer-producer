package com.epam.mentoring.multithreading.util;

import java.io.Console;

public class ConsoleReader {
    private static final Console console;

    static {
        console = System.console();
        if (console == null) {
            throw new IllegalStateException("Cannot access console");
        }
    }

    public static int readIntFromConsole(String s) {
        System.out.print(s);
        return Integer.parseInt(console.readLine());
    }

    public static String readStringFromConsole(String s) {
        System.out.print(s);
        return console.readLine();
    }
}
