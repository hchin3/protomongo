package com.zirakzigil.protomongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zirakzigil.protomongo.model.Person;

public interface PersonRepository extends MongoRepository<Person, String> {

	List<Person> findByFirstName(String firstName);

	List<Person> findByLastName(String lastName);

}
