package com.example.reactivedemo;

import com.example.reactivedemo.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class PersonRepositoryImpl implements PersonRepository {

    Person michael = new Person(1, "Michael", "Weston");
    Person fiona = new Person(2, "Fiona", "Glenanne");
    Person sam = new Person(3, "Sam", "Axe");
    Person jesse = new Person(4, "Jesse", "Porter");

    Map<Integer, Person> personById = new HashMap<>() {{
        put(michael.getId(), michael);
        put(fiona.getId(), fiona);
        put(sam.getId(), sam);
        put(jesse.getId(), jesse);
    }};

    @Override
    public Mono<Person> getById(Integer id) {
        return Mono.just(personById.get(id));
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(michael, fiona, sam, jesse);
    }
}
