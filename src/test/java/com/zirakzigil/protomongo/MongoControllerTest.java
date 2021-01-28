package com.zirakzigil.protomongo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zirakzigil.protomongo.controller.MongoController;
import com.zirakzigil.protomongo.service.NoSqlService;

@WebMvcTest
public class MongoControllerTest {

	@MockBean
	private NoSqlService mongoService;

	@MockBean
	private MongoController mongoController;

	@Autowired
	private MockMvc mockMvc;

	private static ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testConstuctor() {
		assertNotNull(new MongoController(mongoService));
	}

	private class Database {

		private String name;
		private double sizeOnDisk;
		private boolean empty;

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@SuppressWarnings("unused")
		public double getSizeOnDisk() {
			return sizeOnDisk;
		}

		public void setSizeOnDisk(double sizeOnDisk) {
			this.sizeOnDisk = sizeOnDisk;
		}

		@SuppressWarnings("unused")
		public boolean isEmpty() {
			return empty;
		}

		public void setEmpty(boolean empty) {
			this.empty = empty;
		}
	}

	@Test
	public void testListDatabaseNames() throws Exception {

		final String name = "someName";
		final double sizeOnDisk = 3d;
		final boolean empty = true;

		final Document doc = new Document();
		doc.append("name", name);
		doc.append("sizeOnDisk", sizeOnDisk);
		doc.append("empty", empty);

		final List<Document> list = new ArrayList<>();
		list.add(doc);

		Mockito.when(mongoController.listDatabaseNames())
				.thenReturn(new ResponseEntity<List<Document>>(list, HttpStatus.OK));

		final Database db = new Database();
		db.setName(name);
		db.setSizeOnDisk(sizeOnDisk);
		db.setEmpty(empty);

		final List<Database> dbList = new ArrayList<>();
		dbList.add(db);

		final String json = mapper.writeValueAsString(dbList);

		mockMvc.perform(get("/mongo/1/databases").content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$..name", Matchers.equalTo(Lists.newArrayList(name))))
				.andExpect(jsonPath("$..sizeOnDisk", Matchers.equalTo(Lists.newArrayList(sizeOnDisk))))
				.andExpect(jsonPath("$..empty", Matchers.equalTo(Lists.newArrayList(empty))));
	}

}
