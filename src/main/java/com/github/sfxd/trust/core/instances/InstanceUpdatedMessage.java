package com.github.sfxd.trust.core.instances;

import com.github.sfxd.trust.core.Message;
import com.github.sfxd.trust.core.subscription.Subscription;
import com.github.sfxd.trust.core.users.User;
import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;

public class InstanceUpdatedMessage implements Message {
    private final Subscription subscription;
    private final DiffResult<Instance> diff;

    public InstanceUpdatedMessage(Subscription subscription, DiffResult<Instance> diff) {
        this.subscription = subscription;
        this.diff = diff;
    }

    @Override
    public User to() {
        return this.subscription.getUser();
    }

    @Override
    public String body() {
        var body = new StringBuilder();
        for (Diff<?> d : diff) {
            body.append("  %s: %s -> %s%n".formatted(d.getFieldName(), d.getLeft(), d.getRight()));
        }

        return body.toString();
    }
}
