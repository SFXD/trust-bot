package com.github.sfxd.trust;

/**
 * DTO for Discord Gateway api.
 */
public class GatewayResponse<T> {
    @SuppressWarnings("unused")
    private final int op;

    @SuppressWarnings("unused")
    private final T d;

    public GatewayResponse(int op, T d) {
        this.op = op;
        this.d = d;
    }
}
