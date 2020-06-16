package com.company.currency.exception;

import org.slf4j.helpers.MessageFormatter;

public class IllegalArgumentsException extends RuntimeException {

    public IllegalArgumentsException(String message) {
        super(message);
    }

    public IllegalArgumentsException(String message, Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
    }

    public static void throwException(String message, Object... args) {
        throw new IllegalArgumentsException(message, args);
    }
}
