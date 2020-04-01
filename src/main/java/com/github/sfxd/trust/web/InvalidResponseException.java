package com.github.sfxd.trust.web;

/**
 * Represents any error that can happen when the httpclient doesn't
 * return the response you expect.
 */
public class InvalidResponseException extends RuntimeException {
    private static final long serialVersionUID = -2422424845287908196L;

    private final int statusCode;
    public InvalidResponseException(int statusCode) {
        this.statusCode = statusCode;
    }

    public int statusCode() {
        return this.statusCode;
    }
}
