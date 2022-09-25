package com.example.reactivedemo;

import com.example.reactivedemo.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getById() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();

        System.out.println(person.toString());
    }

    @Test
    void getByIdSubscribe() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono.subscribe(person -> {
            System.out.println(person);
        });
    }

    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono.map(person -> {
            System.out.println("Should NOT see this without a subscriber");
            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println(firstName);
        });

    }

    @Test
    void findAll() {
    }
}