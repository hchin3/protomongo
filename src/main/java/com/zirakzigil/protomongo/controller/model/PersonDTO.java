package com.zirakzigil.protomongo.controller.model;

import java.io.Serializable;

import com.zirakzigil.protomongo.model.Person;

public class PersonDTO implements Serializable {

	private static final long serialVersionUID = -4169880146187751925L;

	private String id;

	private String firstName;

	private String lastName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Converts this {@link PersonDTO} to a {@link Person}.
	 * 
	 * @return
	 */
	public Person convertToPerson() {
		return new Person().id(getId()).firstName(getFirstName()).lastName(getLastName());
	}

}
