package com.github.sfxd.trust.instances;

import com.github.sfxd.trust.Message;
import com.github.sfxd.trust.users.Subscription;
import com.github.sfxd.trust.users.User;
import com.github.sfxd.trust.util.Diff;

import java.util.List;

public class InstanceUpdatedMessage implements Message {
    private final Subscription subscription;
    private final List<Diff<?>> diffs;

    public InstanceUpdatedMessage(Subscription subscription, List<Diff<?>> diffs) {
        this.subscription = subscription;
        this.diffs = diffs;
    }

    @Override
    public User to() {
        return this.subscription.user();
    }

    @Override
    public String body() {
        var body = new StringBuilder();
        for (Diff<?> diff : this.diffs) {
            body.append("  %s: %s -> %s%n".formatted(diff.field(), diff.left(), diff.right()));
        }

        return body.toString();
    }
}
