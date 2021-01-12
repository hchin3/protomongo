package com.zirakzigil.protomongo.db;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoDbConfiguration extends AbstractMongoClientConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(MongoDbConfiguration.class);

	private final String uri;
	private final String db;
	private final String collection;

	private MongoClient mongoClient;

	public MongoDbConfiguration(@Value("${spring.data.mongodb.uri}") final String uri,
			@Value("${spring.data.mongodb.database}") final String db,
			@Value("${zz.data.mongodb.collection}") final String collection) {
		this.uri = uri;
		this.db = db;
		this.collection = collection;
	}

	@PostConstruct
	public void init() {

		final CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
		final CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				pojoCodecRegistry);

		final ConnectionString connString = new ConnectionString(uri);

		mongoClient = MongoClients
				.create(MongoClientSettings.builder().applyConnectionString(connString).codecRegistry(codecRegistry)
						.writeConcern(WriteConcern.ACKNOWLEDGED).readConcern(ReadConcern.DEFAULT).build());

	}

	@Bean(name = "databaseName")
	@Override
	protected String getDatabaseName() {
		return db;
	}

	@Bean(name = "collectionName")
	protected String getCollectionName() {
		return collection;
	}

	@Bean
	public MongoClient mongoClient() {
		return mongoClient;
	}

	@PreDestroy
	public void close() {
		if (mongoClient != null) {
			LOG.info("Close mongo client.");
			mongoClient.close();
		}
	}

}
