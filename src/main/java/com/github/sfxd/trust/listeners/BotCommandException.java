package com.github.sfxd.trust.listeners;

class BotCommandException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    BotCommandException(Throwable ex) {
        super(ex);
    }
}
