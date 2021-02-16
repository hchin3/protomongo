package com.zirakzigil.protomongo.controller;

public class Endpoint {

	public static final String MONGO_ENDPOINT_1_ROOT = "/mongo/1";

	public static final String RELATIVE_DATABASES = "/databases";

	public static final String PERSON_ENDPOINT_1_ROOT = "/person/1";

	public static final String RELATIVE_HELLO = "/hello";
	public static final String RELATIVE_COUNT = "/count";
	public static final String RELATIVE_ID = "/{id}";
	public static final String RELATIVE_FIRSTNAME = "/firstName/{firstName}";
	public static final String RELATIVE_LASTNAME = "/lastName/{lastName}";

	private Endpoint() {
		throw new IllegalStateException("REST endpoint constants class.");
	}

}
