package com.github.sfxd.trust.core.instances;

import static com.github.sfxd.trust.core.instances.Instance.Environment.PRODUCTION;
import static com.github.sfxd.trust.core.instances.Instance.Environment.SANDBOX;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class InstanceRepositoryTests {

    private final InstanceRepository instanceRepository = new InstanceRepository();

    @Test
    void it_should_find_instances_whos_key_is_in_the_set() {
        var a = new Instance("NA01", SANDBOX);
        var b = new Instance("CS01", PRODUCTION);

        this.instanceRepository.insert(List.of(a, b));
        List<Instance> found = this.instanceRepository.findByKeyIn(Set.of(a.key()))
            .stream()
            .toList();

        assertEquals(1, found.size());
        assertEquals(found.get(0).key(), a.key());
    }

    @Test
    void it_should_find_instances_whos_id_is_in_the_set() {
        var a = new Instance("NA02", PRODUCTION);
        var b = new Instance("CS02", SANDBOX);

        this.instanceRepository.insert(List.of(a, b));
        List<Instance> found = this.instanceRepository.findByIdIn(Set.of(a.getId()))
            .stream()
            .toList();

        assertEquals(1, found.size());
        assertEquals(found.getFirst().getId(), a.getId());
    }

    @Test
    void it_should_find_the_instance_which_the_matching_key() {
        var a = new Instance("NA03", PRODUCTION);
        var b = new Instance("CS03", SANDBOX);

        this.instanceRepository.insert(List.of(a, b));

        Instance found = this.instanceRepository.findByKey(a.key());
        assertEquals(found.key(), a.key());
    }
}
