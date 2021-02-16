package com.zirakzigil.protomongo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zirakzigil.protomongo.controller.Endpoint;
import com.zirakzigil.protomongo.controller.PersonController;
import com.zirakzigil.protomongo.controller.model.PersonDTO;
import com.zirakzigil.protomongo.model.Person;
import com.zirakzigil.protomongo.service.PersonService;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

	private static final Logger LOG = LoggerFactory.getLogger(PersonControllerTest.class);

	private static final String USER_0_ID = "0";
	private static final String USER_0_FIRSTNAME = "Albert";
	private static final String USER_0_LASTNAME = "Anderson";

	private static final String USER_1_ID = "1";
	private static final String USER_1_FIRSTNAME = "Bernard";
	private static final String USER_1_LASTNAME = "Bell";

	private static final String USER_2_ID = "2";
	private static final String USER_2_FIRSTNAME = "Cassandra";
	private static final String USER_2_LASTNAME = "Cairns";

	private static final ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private PersonService personService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testHello() throws Exception {

		final String url = Endpoint.PERSON_ENDPOINT_1_ROOT + Endpoint.RELATIVE_HELLO;

		mockMvc.perform(get(url)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Hello world!"));
	}

	private Person mockPerson(final String id, final String firstName, final String lastName) {
		return new Person().id(id).firstName(firstName).lastName(lastName);
	}

	private List<Person> mockPersonList() {

		final List<Person> list = new ArrayList<>();

		list.add(mockPerson(USER_0_ID, USER_0_FIRSTNAME, USER_0_LASTNAME));
		list.add(mockPerson(USER_1_ID, USER_1_FIRSTNAME, USER_1_LASTNAME));
		list.add(mockPerson(USER_2_ID, USER_2_FIRSTNAME, USER_2_LASTNAME));

		return list;
	}

	private PersonDTO mockPersonDTO(final String id, final String firstName, final String lastName) {

		final PersonDTO dto = new PersonDTO();
		dto.setId(id);
		dto.setFirstName(firstName);
		dto.setLastName(lastName);

		return dto;
	}

	@Test
	void testPersons() throws Exception {

		Mockito.when(personService.findAll()).thenReturn(mockPersonList());

		LOG.debug(mockMvc.perform(get(Endpoint.PERSON_ENDPOINT_1_ROOT)).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString());

		final String pattern = "$..[?(@.id==\"%s\")].firstName";

		mockMvc.perform(get(Endpoint.PERSON_ENDPOINT_1_ROOT)).andExpect(status().isOk()).andExpect(
				jsonPath(String.format(pattern, USER_0_ID), Matchers.equalTo(Lists.newArrayList(USER_0_FIRSTNAME))));
		mockMvc.perform(get(Endpoint.PERSON_ENDPOINT_1_ROOT)).andExpect(status().isOk()).andExpect(
				jsonPath(String.format(pattern, USER_1_ID), Matchers.equalTo(Lists.newArrayList(USER_1_FIRSTNAME))));
		mockMvc.perform(get(Endpoint.PERSON_ENDPOINT_1_ROOT)).andExpect(status().isOk()).andExpect(
				jsonPath(String.format(pattern, USER_2_ID), Matchers.equalTo(Lists.newArrayList(USER_2_FIRSTNAME))));
	}

	@Test
	void testCount() throws Exception {

		final long count = 10;

		final String url = Endpoint.PERSON_ENDPOINT_1_ROOT + Endpoint.RELATIVE_COUNT;

		Mockito.when(personService.count()).thenReturn(count);
		// Casting to int was necessary to get rid of 'expected <123L> but was <123>'
		mockMvc.perform(get(url)).andExpect(status().isOk())
				.andExpect(jsonPath("$.count", Matchers.equalTo((int) count)));
	}

	@Test
	void testFindByFirstName() throws Exception {

		final Person person = mockPerson(USER_0_ID, USER_0_FIRSTNAME, USER_0_LASTNAME);

		Mockito.when(personService.findByFirstName(Mockito.anyString())).thenReturn(Lists.newArrayList(person));

		final String url = Endpoint.PERSON_ENDPOINT_1_ROOT
				+ Endpoint.RELATIVE_FIRSTNAME.replace("{firstName}", USER_0_FIRSTNAME);

		final String pattern = "$..[?(@.id==\"%s\")].firstName";

		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(
				jsonPath(String.format(pattern, USER_0_ID), Matchers.equalTo(Lists.newArrayList(USER_0_FIRSTNAME))));
	}

	@Test
	void testFindByLastName() throws Exception {

		final Person person = mockPerson(USER_0_ID, USER_0_FIRSTNAME, USER_0_LASTNAME);

		Mockito.when(personService.findByLastName(Mockito.anyString())).thenReturn(Lists.newArrayList(person));

		final String url = Endpoint.PERSON_ENDPOINT_1_ROOT
				+ Endpoint.RELATIVE_LASTNAME.replace("{lastName}", USER_0_LASTNAME);

		final String pattern = "$..[?(@.id==\"%s\")].lastName";

		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(
				jsonPath(String.format(pattern, USER_0_ID), Matchers.equalTo(Lists.newArrayList(USER_0_LASTNAME))));
	}

	@Test
	void testCreate() throws Exception {

		Mockito.when(personService.create(Mockito.any(Person.class))).thenReturn(USER_0_ID);

		final String expression = "$.id";

		final PersonDTO dto = mockPersonDTO(null, USER_0_FIRSTNAME, USER_0_LASTNAME);
		final String json = mapper.writeValueAsString(dto);
		mockMvc.perform(post(Endpoint.PERSON_ENDPOINT_1_ROOT).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andExpect(jsonPath(expression, Matchers.equalTo(USER_0_ID)));
	}

	@Test
	void testRead() throws Exception {

		final Person person = mockPerson(USER_0_ID, USER_0_FIRSTNAME, USER_0_LASTNAME);

		Mockito.when(personService.read(USER_0_ID)).thenReturn(Optional.of(person));

		final String expression = "$.firstName";

		final String url = Endpoint.PERSON_ENDPOINT_1_ROOT + Endpoint.RELATIVE_ID.replace("{id}", USER_0_ID);
		mockMvc.perform(get(url)).andExpect(status().isOk())
				.andExpect(jsonPath(expression, Matchers.equalTo(USER_0_FIRSTNAME)));
	}

	@Test
	void testUpdate() throws Exception {

		Mockito.when(personService.update(Mockito.any(Person.class))).thenReturn(USER_0_ID);

		final PersonDTO dto = mockPersonDTO(USER_0_ID, USER_0_FIRSTNAME, USER_0_LASTNAME);
		final String json = mapper.writeValueAsString(dto);

		final String url = Endpoint.PERSON_ENDPOINT_1_ROOT + Endpoint.RELATIVE_ID.replace("{id}", USER_0_ID);

		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNoContent());
	}

	@Test
	void testDelete() throws Exception {
		final String url = Endpoint.PERSON_ENDPOINT_1_ROOT + Endpoint.RELATIVE_ID.replace("{id}", USER_0_ID);
		mockMvc.perform(delete(url)).andExpect(status().isNoContent());
	}

}
