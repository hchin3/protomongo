package com.zirakzigil.protomongo.controller.model;

import java.io.Serializable;

import com.zirakzigil.protomongo.controller.PersonController;

/**
 * Response class for {@link PersonController#personCount()}.
 * 
 * @author harry
 */
public class PersonCountResponse implements Serializable {

	private static final long serialVersionUID = -816696658277601757L;

	private long count;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public PersonCountResponse count(long count) {
		this.count = count;
		return this;
	}

}