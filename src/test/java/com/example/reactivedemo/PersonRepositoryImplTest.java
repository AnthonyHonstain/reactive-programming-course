package com.example.reactivedemo;

import com.example.reactivedemo.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlocking() {
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
    void getByIdMapFunctionWithSubscribe() {
        Mono<Person> personMono = personRepository.getById(2);
        personMono.map(person -> {
            return person.getFirstName();
        }).subscribe(firstName -> {
            assertEquals("Fiona", firstName);
        });
    }

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person.toString());
    }

    @Test
    void fluxTestSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void fluxTestToListMono() {
        Flux<Person> personFlux = personRepository.findAll();
        Mono<List<Person>> personListMono = personFlux.collectList();
        personListMono.subscribe(list -> {
            list.forEach(person -> {
                System.out.println(person.toString());
            });
        });
    }

    @Test
    void fluxTestMapFunction() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.map(person -> {
            System.out.println("Should NOT see this without a subscriber");
            return person.getFirstName();
        }).subscribe(name -> {
            System.out.println(name);
        });
    }

    @Test
    void testFindPersonById() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 3;
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();
        // NOTE - next operation handles things quietly.
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8;
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8;
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single();
        personMono.doOnError(throwable -> {
            // Example of taking some action on the error, BUT the exception will still happen
            System.out.println("Error happened");
        }).onErrorReturn(
                // Forcing a default to happen, using the builder to make a default.
                Person.builder().id(id).build()
        ).subscribe(person -> {
            System.out.println(person.toString());
        });
    }
}