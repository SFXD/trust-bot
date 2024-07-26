package com.github.sfxd.trust.core.instances;

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
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA01");
        b.setKey("CS01");

        this.instanceRepository.insert(List.of(a, b));
        List<Instance> found = this.instanceRepository.findByKeyIn(Set.of(a.getKey()))
            .stream()
            .toList();

        assertEquals(1, found.size());
        assertEquals(found.get(0).getKey(), a.getKey());
    }

    @Test
    void it_should_find_instances_whos_id_is_in_the_set() {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA02");
        b.setKey("CS02");

        this.instanceRepository.insert(List.of(a, b));
        List<Instance> found = this.instanceRepository.findByIdIn(Set.of(a.getId()))
            .stream()
            .toList();

        assertEquals(1, found.size());
        assertEquals(found.get(0).getId(), a.getId());
    }

    @Test
    void it_should_find_the_instance_which_the_matching_key() {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA03");
        b.setKey("CS03");

        this.instanceRepository.insert(List.of(a, b));

        Instance found = this.instanceRepository.findByKey(a.getKey());
        assertEquals(found.getKey(), a.getKey());
    }
}
