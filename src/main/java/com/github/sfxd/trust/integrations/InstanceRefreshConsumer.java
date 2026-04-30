// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.integrations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.github.sfxd.trust.instances.Instance;
import com.github.sfxd.trust.instances.InstanceRepository;
import com.github.sfxd.trust.util.Diff;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import static java.util.function.Function.identity;

@Singleton
public class InstanceRefreshConsumer implements Consumer<Collection<InstancePreviewViewModel>> {

    private final InstanceRepository instanceRepository;

    @Inject
    public InstanceRefreshConsumer(InstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    @Override
    public void accept(Collection<InstancePreviewViewModel> incomingInstances) {
        Map<String, Instance> instancePreviews = incomingInstances.stream()
            .map(InstancePreviewViewModel::toInstance)
            .collect(Collectors.toMap(Instance::key, identity()));

        Map<String, Instance> instances = this.instanceRepository.findByKeyIn(instancePreviews.keySet())
            .stream()
            .collect(Collectors.toMap(Instance::key, identity()));
        var forUpdate = new ArrayList<Instance>();
        for (Instance preview : instancePreviews.values()) {
            Instance current = instances.computeIfAbsent(preview.key(), _ -> preview);
            List<Diff<?>> diffs = current.diff(preview);
            if (!diffs.isEmpty()) {
                this.update(current, preview);
                forUpdate.add(current);
            }
        }

        this.instanceRepository.save(forUpdate);
    }

    private void update(Instance current, Instance preview) {
        current.setLocation(preview.location());
        current.setReleaseVersion(preview.releaseVersion());
        current.setReleaseNumber(preview.releaseNumber());
        current.setStatus(preview.status());
    }
}
