package com.zirakzigil.protomongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClients;
import com.zirakzigil.protomongo.model.Person;
import com.zirakzigil.protomongo.service.PersonService;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@SpringBootTest
@ActiveProfiles("test")
class PersonServiceTest {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PersonServiceTest.class);

	@Value("${spring.data.mongodb.uri}")
	private String uri;

	@Value("${spring.data.mongodb.database}")
	private String db;

	@Value("${zz.data.mongodb.host}")
	private String host;

	@Value("${zz.data.mongodb.port}")
	private int port;

	@Autowired
	private PersonService personService;

	private MongodExecutable mongodExecutable;
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void before() throws Exception {

		final IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net(host, port, Network.localhostIsIPv6())).build();

		final MongodStarter starter = MongodStarter.getDefaultInstance();
		mongodExecutable = starter.prepare(mongodConfig);
		mongodExecutable.start();
		mongoTemplate = new MongoTemplate(MongoClients.create(uri), db);
	}

	@AfterEach
	void clean() {
		mongodExecutable.stop();
	}

	@Test
	void testFindAll() {

		// given
		DBObject objectToSave = BasicDBObjectBuilder.start().add("firstName", "Billy").add("lastName", "Boy").get();

		// when
		mongoTemplate.save(objectToSave, "person");

		// then
		assertThat(mongoTemplate.findAll(DBObject.class, "person")).extracting("firstName").containsOnly("Billy");

		final List<Person> list = personService.findAll();

		assertNotNull(list);
	}

}
