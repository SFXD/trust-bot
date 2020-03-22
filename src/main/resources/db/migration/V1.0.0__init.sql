-- trust-bot a discord bot to watch the salesforce trust api.
-- Copyright (C) 2020 George Doenlen

-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.

-- // You should have received a copy of the GNU General Public License
-- // along with this program.  If not, see <https://www.gnu.org/licenses/>.

CREATE TABLE Instance (
    id IDENTITY NOT NULL,
    key VARCHAR(255) NOT NULL UNIQUE,
    location VARCHAR(255),
    releaseVersion VARCHAR(255),
    releaseNumber VARCHAR(255),
    environment VARCHAR(20),
    status VARCHAR(255)
);

CREATE TABLE Subscriber (
    id IDENTITY NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE InstanceSubscriber (
    id IDENTITY NOT NULL,
    instanceId BIGINT NOT NULL,
    subscriberId BIGINT NOT NULL,
    FOREIGN KEY (instanceId) REFERENCES Instance(id),
    FOREIGN KEY (subscriberId) REFERENCES Subscriber(id)
);
