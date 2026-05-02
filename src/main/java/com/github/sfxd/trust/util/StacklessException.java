package com.github.sfxd.trust.util;

public class StacklessException extends Exception {
    public StacklessException(String message) {
        super(message, null, false, true);
    }

    public StacklessException() {
        this("");
    }
}
