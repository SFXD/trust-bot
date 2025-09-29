// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.integrations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.core.Messages;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceRepository;
import com.github.sfxd.trust.core.instances.InstanceUpdatedMessage;
import com.github.sfxd.trust.core.users.Subscription;
import com.github.sfxd.trust.util.Diff;

import static java.util.function.Function.identity;

@ApplicationScoped
class InstanceRefreshConsumer implements Consumer<Collection<InstancePreviewViewModel>> {

    private final InstanceRepository instanceRepository;
    private final Messages messages;

    @Inject
    InstanceRefreshConsumer(InstanceRepository instanceRepository, Messages messages) {
        this.instanceRepository = instanceRepository;
        this.messages = messages;
    }

    @Override
    public void accept(Collection<InstancePreviewViewModel> incomingInstances) {
        Map<String, Instance> instancePreviews = incomingInstances.stream()
            .map(InstancePreviewViewModel::toInstance)
            .collect(Collectors.toMap(Instance::key, identity()));

        Map<String, Instance> instances = this.instanceRepository.findByKeyIn(instancePreviews.keySet())
            .stream()
            .collect(Collectors.toMap(Instance::key, identity()));

        for (Instance preview : instancePreviews.values()) {
            Instance current = instances.computeIfAbsent(preview.key(), key -> preview);
            List<Diff<?>> diffs = current.diff(preview);
            if (!diffs.isEmpty()) {
                this.update(current, preview, diffs);
            }
        }

        this.instanceRepository.save(instances.values());
    }

    private void update(Instance current, Instance preview, List<Diff<?>> diffs) {
        current.setLocation(preview.location());
        current.setReleaseVersion(preview.releaseVersion());
        current.setReleaseNumber(preview.releaseNumber());
        current.setStatus(preview.status());
        this.sendMessages(current, diffs);
    }

    private void sendMessages(Instance current, List<Diff<?>> diffs) {
        for (Subscription subscription : current.getSubscriptions()) {
            this.messages.send(new InstanceUpdatedMessage(subscription, diffs));
        }
    }
}
