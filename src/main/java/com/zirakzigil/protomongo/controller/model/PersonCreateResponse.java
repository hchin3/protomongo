package com.zirakzigil.protomongo.controller.model;

import java.io.Serializable;

import com.zirakzigil.protomongo.controller.PersonController;
import com.zirakzigil.protomongo.model.Person;

/**
 * Response class for {@link PersonController#create(Person)}.
 * 
 * @author harry
 */
public class PersonCreateResponse implements Serializable {

	private static final long serialVersionUID = 8405529975686707152L;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PersonCreateResponse id(String id) {
		this.id = id;
		return this;
	}

}
