package com.zirakzigil.protomongo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.zirakzigil.protomongo.controller.Endpoint;
import com.zirakzigil.protomongo.controller.MongoController;
import com.zirakzigil.protomongo.service.MongoService;

@WebMvcTest(MongoController.class)
class MongoControllerTest {

	@MockBean
	private MongoService mongoService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testListDatabaseNames() throws Exception {

		final String name = "someName";
		final double sizeOnDisk = 3d;
		final boolean empty = true;

		final Document doc = new Document();
		doc.append("name", name);
		doc.append("sizeOnDisk", sizeOnDisk);
		doc.append("empty", empty);

		final List<Document> list = new ArrayList<>();
		list.add(doc);

		Mockito.when(mongoService.getDatabaseNames()).thenReturn(list);

		final String url = Endpoint.MONGO_ENDPOINT_1_ROOT + Endpoint.RELATIVE_DATABASES;

		mockMvc.perform(get(url)).andExpect(status().isOk())
				.andExpect(jsonPath("$..name", Matchers.equalTo(Lists.newArrayList(name))))
				.andExpect(jsonPath("$..sizeOnDisk", Matchers.equalTo(Lists.newArrayList(sizeOnDisk))))
				.andExpect(jsonPath("$..empty", Matchers.equalTo(Lists.newArrayList(empty))));
	}

}
