package com.zirakzigil.protomongo.controller;

import static com.zirakzigil.protomongo.controller.Endpoint.PERSON_ENDPOINT_1_ROOT;
import static com.zirakzigil.protomongo.controller.Endpoint.RELATIVE_COUNT;
import static com.zirakzigil.protomongo.controller.Endpoint.RELATIVE_FIRSTNAME;
import static com.zirakzigil.protomongo.controller.Endpoint.RELATIVE_HELLO;
import static com.zirakzigil.protomongo.controller.Endpoint.RELATIVE_ID;
import static com.zirakzigil.protomongo.controller.Endpoint.RELATIVE_LASTNAME;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zirakzigil.protomongo.controller.model.PersonCountResponse;
import com.zirakzigil.protomongo.controller.model.PersonCreateResponse;
import com.zirakzigil.protomongo.controller.model.PersonDTO;
import com.zirakzigil.protomongo.model.Person;
import com.zirakzigil.protomongo.service.PersonService;

@RestController
@RequestMapping(PERSON_ENDPOINT_1_ROOT)
public class PersonController {

	private final PersonService personService;

	public PersonController(@Autowired final PersonService personService) {
		this.personService = personService;
	}

	@GetMapping(RELATIVE_HELLO)
	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("Hello world!");
	}

	@GetMapping("")
	public ResponseEntity<List<Person>> persons() {
		return ResponseEntity.ok(personService.findAll());
	}

	@GetMapping(RELATIVE_COUNT)
	public ResponseEntity<PersonCountResponse> count() {
		return ResponseEntity.ok(new PersonCountResponse().count(personService.count()));
	}

	@GetMapping(RELATIVE_FIRSTNAME)
	public ResponseEntity<List<Person>> findByFirstName(@PathVariable(name = "firstName") final String firstName) {
		return ResponseEntity.ok(personService.findByFirstName(firstName));
	}

	@GetMapping(RELATIVE_LASTNAME)
	public ResponseEntity<List<Person>> findByLastName(@PathVariable(name = "lastName") final String lastName) {
		return ResponseEntity.ok(personService.findByLastName(lastName));
	}

	@PostMapping("")
	public ResponseEntity<PersonCreateResponse> create(@RequestBody @NonNull final PersonDTO dto) {
		Preconditions.checkArgument(dto.getId() == null, "Id must be null to create new Person.");
		return ResponseEntity.ok(new PersonCreateResponse().id(personService.create(dto.convertToPerson())));
	}

	@GetMapping(RELATIVE_ID)
	public ResponseEntity<Person> read(@PathVariable(name = "id") final String id) {

		final Optional<Person> person = personService.read(id);

		if (person.isPresent()) {
			return ResponseEntity.ok(person.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping(RELATIVE_ID)
	public ResponseEntity<Void> update(@PathVariable(name = "id") final String id,
			@RequestBody @NonNull final PersonDTO dto) {

		Preconditions.checkArgument(!Strings.isNullOrEmpty(dto.getId()), "Person.id cannot be blank.");

		personService.update(dto.convertToPerson());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(RELATIVE_ID)
	public ResponseEntity<Void> delete(@PathVariable(name = "id") final String id) {
		personService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
