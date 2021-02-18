package com.zirakzigil.protomongo.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClient;

@Service
public class MongoService {

	private final MongoClient mongoClient;

	public MongoService(@Autowired final MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public List<Document> getDatabaseNames() {
		return StreamSupport.stream(mongoClient.listDatabases().spliterator(), false).collect(Collectors.toList());
	}

}
