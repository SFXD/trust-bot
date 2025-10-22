package com.github.sfxd.trust.integrations;

import com.github.sfxd.trust.instances.Instance;
import com.github.sfxd.trust.instances.Instance.Environment;
import io.avaje.jsonb.Json;

import static com.github.sfxd.trust.instances.Instance.Environment.PRODUCTION;
import static com.github.sfxd.trust.instances.Instance.Environment.SANDBOX;

@Json
record InstancePreviewViewModel(
    String key,
    String location,
    String releaseVersion,
    String releaseNumber,
    String status,
    String environment
) {
    public Instance toInstance() {
        var instance = new Instance(this.key, this.env());
        instance.setLocation(this.location);
        instance.setReleaseVersion(this.releaseVersion);
        instance.setReleaseNumber(releaseNumber);
        instance.setStatus(this.status);

        return instance;
    }

    private Environment env() {
        return switch (this.environment) {
            case "sandbox" -> SANDBOX;
            case "production" -> PRODUCTION;
            default -> throw new IllegalArgumentException(this.environment);
        };
    }
}
