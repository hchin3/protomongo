package com.zirakzigil.protomongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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

	private static final String ID = "id";
	private static final String FIRSTNAME = "firstName";
	private static final String LASTNAME = "lastName";

	private static final String USER_0_FIRSTNAME = "Albert";
	private static final String USER_0_LASTNAME = "Anderson";

	private static final String USER_1_FIRSTNAME = "Bernard";
	private static final String USER_1_LASTNAME = "Bell";

	private static final String USER_2_FIRSTNAME = "Cassandra";
	private static final String USER_2_LASTNAME = "Cairns";

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

	private String id0;
	private String id1;
	private String id2;

	private String insertPerson(final String firstName, final String lastName) {

		final DBObject objectToSave = BasicDBObjectBuilder.start().add(FIRSTNAME, firstName).add(LASTNAME, lastName)
				.get();
		final DBObject savedObject = mongoTemplate.save(objectToSave, "person");
		// assertThat(mongoTemplate.findAll(DBObject.class,
		// "person")).extracting(FIRSTNAME).containsOnly(firstName);

		return ((ObjectId) savedObject.get("_id")).toString();
	}

	@BeforeEach
	void before() throws Exception {

		final IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net(host, port, Network.localhostIsIPv6())).build();

		final MongodStarter starter = MongodStarter.getDefaultInstance();
		mongodExecutable = starter.prepare(mongodConfig);
		mongodExecutable.start();
		mongoTemplate = new MongoTemplate(MongoClients.create(uri), db);

		id0 = insertPerson(USER_0_FIRSTNAME, USER_0_LASTNAME);
		id1 = insertPerson(USER_1_FIRSTNAME, USER_1_LASTNAME);
		id2 = insertPerson(USER_2_FIRSTNAME, USER_2_LASTNAME);
	}

	@AfterEach
	void clean() {
		mongodExecutable.stop();
	}

	@Test
	void testFindAll() {

		final List<Person> list = personService.findAll();

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(3, list.size());
	}

	@Test
	void testFindAllPaged() {

		final List<Person> list = personService.findAll(PageRequest.of(0, 2));

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
	}

	@Test
	void testFindByFirstName() {

		final List<Person> list = personService.findByFirstName(USER_1_FIRSTNAME);

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());

		final Person person = list.get(0);

		assertEquals(id1, person.getId());
	}

	@Test
	void testFindByLastName() {

		final List<Person> list = personService.findByLastName(USER_2_LASTNAME);

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());

		final Person person = list.get(0);

		assertEquals(id2, person.getId());
	}

	@Test
	void testCount() {
		assertEquals(3, personService.count());
	}

	@Test
	void testCreate() {

	}

	@Test
	void testRead() {

	}

	@Test
	void testUpdate() {

	}

	@Test
	void testDelete() {

		{
			final List<Person> list = personService.findAll();

			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(3, list.size());
		}

		personService.delete(id1);

		{
			final List<Person> list = personService.findAll();

			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(2, list.size());
		}

	}
}
