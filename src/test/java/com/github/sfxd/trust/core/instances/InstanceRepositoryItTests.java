package com.github.sfxd.trust.core.instances;

import static com.github.sfxd.trust.instances.Instance.Environment.PRODUCTION;
import static com.github.sfxd.trust.instances.Instance.Environment.SANDBOX;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import jakarta.inject.Inject;

import io.avaje.inject.test.InjectTest;
import org.junit.jupiter.api.Test;

import com.github.sfxd.trust.instances.Instance;
import com.github.sfxd.trust.instances.InstanceRepository;

@InjectTest
public class InstanceRepositoryItTests {

    @Inject
    public InstanceRepository instanceRepository;

    @Test
    void it_should_find_instances_whos_key_is_in_the_set() {
        var a = new Instance("NA01", SANDBOX);
        var b = new Instance("CS01", PRODUCTION);

        this.instanceRepository.save(List.of(a, b));
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

        this.instanceRepository.save(List.of(a, b));
        List<Instance> found = this.instanceRepository.findByIdIn(Set.of(a.id()))
            .stream()
            .toList();

        assertEquals(1, found.size());
        assertEquals(found.getFirst().id(), a.id());
    }

    @Test
    void it_should_find_the_instance_which_the_matching_key() {
        var a = new Instance("NA03", PRODUCTION);
        var b = new Instance("CS03", SANDBOX);

        this.instanceRepository.save(List.of(a, b));

        Instance found = this.instanceRepository.findByKey(a.key());
        assertEquals(found.key(), a.key());
    }
}
