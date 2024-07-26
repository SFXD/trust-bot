// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.integrations;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.core.Messages;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceRepository;
import com.github.sfxd.trust.core.instances.InstanceUpdatedMessage;
import com.github.sfxd.trust.core.subscription.Subscription;
import org.apache.commons.lang3.builder.DiffResult;

import static java.util.function.Function.identity;

@ApplicationScoped
class InstanceRefreshConsumer implements Consumer<Collection<Instance>> {

    private final InstanceRepository instanceRepository;
    private final Messages messages;

    @Inject
    InstanceRefreshConsumer(InstanceRepository instanceRepository, Messages messages) {
        this.instanceRepository = instanceRepository;
        this.messages = messages;
    }

    @Override
    public void accept(Collection<Instance> incomingInstances) {
        Map<String, Instance> instancePreviews = incomingInstances.stream()
            .collect(Collectors.toMap(Instance::getKey, identity()));

        Map<String, Instance> instances = this.instanceRepository.findByKeyIn(instancePreviews.keySet())
            .stream()
            .collect(Collectors.toMap(Instance::getKey, identity()));

        for (Instance preview : instancePreviews.values()) {
            Instance current = instances.computeIfAbsent(preview.getKey(), key -> preview);
            DiffResult<Instance> diff = current.diff(preview);
            if (!diff.getDiffs().isEmpty())
                this.update(current, preview, diff);
        }

        this.instanceRepository.save(instances.values());
    }

    private void update(Instance current, Instance preview, DiffResult<Instance> diff) {
        current
            .setLocation(preview.getLocation())
            .setReleaseVersion(preview.getReleaseVersion())
            .setReleaseNumber(preview.getReleaseNumber())
            .setStatus(preview.getStatus())
            .setEnvironment(preview.getEnvironment());
        this.sendMessages(current, diff);
    }

    private void sendMessages(Instance current, DiffResult<Instance> diff) {
        for (Subscription subscription : current.getSubscriptions()) {
            this.messages.send(new InstanceUpdatedMessage(subscription, diff));
        }
    }
}
