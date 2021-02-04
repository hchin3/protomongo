package com.zirakzigil.protomongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.zirakzigil.protomongo.service.MongoService;
import com.zirakzigil.protomongo.service.NoSqlService;

@Configuration
public class ProtomongoAppConfig {

	private final MongoClient mongoClient;

	public ProtomongoAppConfig(@Autowired final MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	@Bean
	public NoSqlService mongoService() {
		return new MongoService(mongoClient);
	}

}
