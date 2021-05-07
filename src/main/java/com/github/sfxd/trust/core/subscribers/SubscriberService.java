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

package com.github.sfxd.trust.core.subscribers;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.core.AbstractEntityService;

import io.ebean.Database;
import io.ebean.annotation.Transactional;

/**
 * Service for interacting with the {@link Subscriber} model.
 */
@Singleton
@Transactional
public class SubscriberService extends AbstractEntityService<Subscriber> {

    @Inject
    public SubscriberService(Database db) {
        super(db, Subscriber.class);
    }
}
