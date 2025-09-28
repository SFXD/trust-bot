package com.github.sfxd.trust.util;

public class StacklessException extends Exception {
    public StacklessException(String message) {
        super(message);
    }

    public StacklessException() {
        this("");
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
