package com.zirakzigil.protomongo.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.client.MongoClient;

public class MongoService implements NoSqlService {

	private final MongoClient mongoClient;

	public MongoService(@Autowired final MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	@Override
	public List<Document> getDatabaseNames() {
		return StreamSupport.stream(mongoClient.listDatabases().spliterator(), false).collect(Collectors.toList());
	}

}
