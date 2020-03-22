package com.github.sfxd.trust.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.InstanceSubscriber;

import net.dv8tion.jda.api.JDA;

/**
 * Service for working with the {@link Instance} model.
 */
@Singleton
@Transactional
public class InstanceService extends AbstractEntityService<Instance> {

    private final JDA jda;
    private final InstanceSubscriberService instanceSubcriberService;

    @Inject
    public InstanceService(EntityManager em, JDA jda, InstanceSubscriberService instanceSubscriberService) {
        super(em, Instance.class);

        Objects.requireNonNull(jda);
        Objects.requireNonNull(instanceSubscriberService);

        this.jda = jda;
        this.instanceSubcriberService = instanceSubscriberService;
    }

    /** {@inheritDoc} */
    @Override
    public List<Instance> update(List<Instance> entities) throws DmlException {
        Map<Long, Instance> instancesById = entities.stream()
            .collect(Collectors.toMap(Instance::getId, Function.identity()));

        Set<Long> notOkIds = this.findByIdIn(instancesById.keySet())
            .stream()
            .filter(old -> {
                Instance current = instancesById.get(old.getId());
                return old.getStatus().equals(Instance.STATUS_OK) && !current.getStatus().equals(Instance.STATUS_OK);
            })
            .map(Instance::getId)
            .collect(Collectors.toSet());

        Map<String, List<InstanceSubscriber>> subscriptionsBySubscriber = this.instanceSubcriberService
            .findByInstanceIdIn(notOkIds)
            .stream()
            .collect(Collectors.groupingBy(is -> is.getSubscriber().getUsername()));

        for (Entry<String, List<InstanceSubscriber>> entry : subscriptionsBySubscriber.entrySet()) {
            var message = new StringBuilder();
            for (InstanceSubscriber is : entry.getValue()) {
                Instance instance = is.getInstance();
                message.append(String.format(
                    "%s: %s\n",
                    instance.getKey(),
                    instancesById.get(instance.getId()).getStatus()
                ));
            }

            this.jda.retrieveUserById(entry.getKey()).queue(user -> {
                user.openPrivateChannel().queue(channel -> channel.sendMessage(message.toString()).queue());
            });
        }

        return super.update(entities);
    }

    /** {@inheritDoc} */
    @Override
    public Instance update(Instance entity) throws DmlException {
        return this.update(List.of(entity)).get(0);
    }

    /**
     * Finds an instance by its unique key (i.e. NA99, CS104)
     *
     * @param key the instance's unique key you want to find
     * @return An optional containing the instance if found or none if hibernate
     *         throws NoResultException
     */
    public Optional<Instance> findByKey(String key) {
        TypedQuery<Instance> query = this.em.createQuery("SELECT i FROM Instance i WHERE key = :key", this.clazz)
            .setParameter("key", key);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    /**
     * Finds all Instances whose key field is in the given set of keys.
     *
     * @param keys the keys you want to filter by
     * @return the matching instances
     */
    public List<Instance> findByKeyIn(Set<String> keys) {
        return this.em.createQuery("SELECT i FROM Instance i WHERE key IN :keys", this.clazz)
            .setParameter("keys", keys)
            .getResultList();
    }

    /**
     * Finds all Instances whose id field is in the given set of ids
     *
     * @param ids the ids you want to filter by
     * @return the matching instances
     */
    public List<Instance> findByIdIn(Set<Long> ids) {
        return this.em.createQuery("SELECT i from Instance i WHERE id IN :ids", this.clazz)
            .setParameter("ids", ids)
            .getResultList();
    }
}
