// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2020 George Doenlen

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package com.github.sfxd.trust.core.instancesubscribers;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.core.EntityService;

/**
 * Service for working with the {@link InstanceSubcriber} model
 */
@Singleton
public class InstanceSubscriberService extends EntityService<InstanceSubscriber> {

    private final InstanceSubscriberRepository instanceSubscriberRepository;

    @Inject
    public InstanceSubscriberService(InstanceSubscriberRepository repository) {
        super(repository);

        this.instanceSubscriberRepository = repository;
    }

    public Optional<InstanceSubscriber> findByInstanceIdAndSubscriberId(Long instanceId, Long subscriberId) {
        return this.instanceSubscriberRepository.findByInstanceIdAndSubscriberId(instanceId, subscriberId);
    }

    public Stream<InstanceSubscriber> findByInstanceIdIn(Collection<Long> instanceIds) {
        return this.instanceSubscriberRepository.findByInstanceIdIn(instanceIds);
    }

    public Optional<InstanceSubscriber> findByKeyAndUsername(String key, String username) {
        return this.instanceSubscriberRepository.findByKeyAndUsername(key, username);
    }
}
