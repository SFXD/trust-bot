package com.github.sfxd.trust.core.instances;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class InstanceFinderTests {

    private InstanceFinder instanceFinder = new InstanceFinder();

    @Test
    void it_should_find_instances_whos_key_is_in_the_set() {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA01");
        b.setKey("CS01");

        this.instanceFinder.insert(List.of(a, b));
        List<Instance> found = this.instanceFinder.findByKeyIn(Set.of(a.getKey())).collect(Collectors.toList());

        assertEquals(1, found.size());
        assertEquals(found.get(0).getKey(), a.getKey());
    }

    @Test
    void it_should_find_instances_whos_id_is_in_the_set() {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA02");
        b.setKey("CS02");

        this.instanceFinder.insert(List.of(a, b));
        List<Instance> found = this.instanceFinder.findByIdIn(Set.of(a.getId())).collect(Collectors.toList());

        assertEquals(1, found.size());
        assertEquals(found.get(0).getId(), a.getId());
    }

    @Test
    void it_should_find_the_instance_which_the_matching_key() {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA03");
        b.setKey("CS03");

        this.instanceFinder.insert(List.of(a, b));

        Instance found = this.instanceFinder.findByKey(a.getKey()).get();
        assertEquals(found.getKey(), a.getKey());
    }

    @Test
    void it_should_return_an_empty_optional_when_no_result_is_found_by_key() {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA04");
        b.setKey("CS04");

        this.instanceFinder.insert(List.of(a, b));

        Optional<Instance> found = this.instanceFinder.findByKey("ZZ00");
        assertTrue(found.isEmpty());
    }
}
