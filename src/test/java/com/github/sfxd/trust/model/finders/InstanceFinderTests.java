package com.github.sfxd.trust.model.finders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.services.InstanceService;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.ebean.DB;
import net.dv8tion.jda.api.JDA;

@TestInstance(Lifecycle.PER_CLASS)
class InstanceFinderTests {

    private InstanceService instanceService;
    private InstanceFinder instanceFinder = new InstanceFinder();

    @BeforeAll
    void beforeAll() {
        this.instanceService = new InstanceService(
            DB.getDefault(),
            mock(JDA.class),
            new InstanceSubscriberFinder(),
            this.instanceFinder
        );
    }

    @Test
    void it_should_find_instances_whos_key_is_in_the_set() throws DmlException {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA01");
        b.setKey("CS01");

        this.instanceService.insert(List.of(a, b));
        List<Instance> found = this.instanceFinder.findByKeyIn(Set.of(a.getKey())).findList();

        assertTrue(found.size() == 1);
        assertTrue(found.get(0).getKey().equals(a.getKey()));
    }

    @Test
    void it_should_find_instances_whos_id_is_in_the_set() throws DmlException {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA02");
        b.setKey("CS02");

        this.instanceService.insert(List.of(a, b));
        List<Instance> found = this.instanceFinder.findByIdIn(Set.of(a.getId())).findList();

        assertTrue(found.size() == 1);
        assertTrue(found.get(0).getId().equals(a.getId()));
    }

    @Test
    void it_should_find_the_instance_which_the_matching_key() throws DmlException {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA03");
        b.setKey("CS03");

        this.instanceService.insert(List.of(a, b));

        Instance found = this.instanceFinder.findByKey(a.getKey()).findOne();
        assertTrue(found.getKey().equals(a.getKey()));
    }

    @Test
    void it_should_return_an_empty_optional_when_no_result_is_found_by_key() throws DmlException {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA04");
        b.setKey("CS04");

        this.instanceService.insert(List.of(a, b));

        Optional<Instance> found = this.instanceFinder.findByKey("ZZ00").findOneOrEmpty();
        assertTrue(found.isEmpty());
    }
}
