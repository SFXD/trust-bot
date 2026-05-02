package com.github.sfxd.trust.inject;

public record EbeanConfig(
    String datasourceUrl,
    String driver,
    String username,
    String password
) {
}
