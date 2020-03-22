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
