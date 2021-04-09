package com.zirakzigil.protomongo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.zirakzigil.protomongo.aop.TrackDuration;
import com.zirakzigil.protomongo.model.Person;
import com.zirakzigil.protomongo.repository.PersonRepository;

@Service
public class PersonService {

	private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

	private final PersonRepository repository;

	public PersonService(@Autowired final PersonRepository repository) {
		this.repository = Objects.requireNonNull(repository, "repository cannot be null.");
		LOG.info("{} instantiated.", getClass().getName());
	}

	/**
	 * Returns all {@link Person} entries in the repository.
	 * <p>
	 * This is not scalable so a solution implementing paging is required.
	 * 
	 * @return
	 */
	@TrackDuration
	public List<Person> findAll() {
		return repository.findAll();
	}

	public List<Person> findAll(final Pageable pageable) {

		final Page<Person> page = repository.findAll(pageable);

		return page.getContent();
	}

	/**
	 * Returns all {@link Person} entries which match the given first name.
	 * 
	 * @param firstName
	 * @return
	 */
	public List<Person> findByFirstName(final String firstName) {
		return repository.findByFirstName(firstName);
	}

	/**
	 * Returns all {@link Person} entries which match the given last name.
	 * 
	 * @param lastName
	 * @return
	 */
	public List<Person> findByLastName(final String lastName) {
		return repository.findByLastName(lastName);
	}

	/**
	 * Returns the record count in the repository.
	 * 
	 * @return
	 */
	public long count() {
		return repository.count();
	}

	/**
	 * Create a new entry for the given {@link Person}.
	 * 
	 * @param person
	 * @return
	 */
	public String create(final Person person) {

		final String id = Objects.requireNonNull(person, () -> "person cannot be null.").getId();

		Preconditions.checkArgument(id == null, "id must be null.");

		return save(person, new Person());
	}

	/**
	 * Returns {@link Person} with the given <code>id</code>.
	 * 
	 * @param id
	 * @return
	 */
	public Optional<Person> read(final String id) {
		return repository.findById(id);
	}

	/**
	 * Applies values in the given {@link Person} to the repository.
	 * 
	 * @param person From client.
	 * @return
	 */
	public String update(final Person person) {

		final Optional<Person> clean = repository.findById(person.getId());

		Preconditions.checkState(clean.isPresent(), "Person not found for id.");

		return save(person, clean.get());
	}

	/**
	 * Delete {@link Person} from repository with the given <code>id</code>.
	 * 
	 * @param id
	 */
	public void delete(final String id) {
		repository.deleteById(id);
	}

	/**
	 * Applies changes from client to DB entry.
	 * 
	 * @param dirty {@link Person} with changes.
	 * @param clean {@link Person} from repository to which changes are applied.
	 * @return
	 */
	private String save(final Person dirty, final Person clean) {
		clean.firstName(dirty.getFirstName()).lastName(dirty.getLastName());
		return repository.save(clean).getId();
	}

}
