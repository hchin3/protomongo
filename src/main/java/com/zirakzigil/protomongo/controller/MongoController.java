package com.zirakzigil.protomongo.controller;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zirakzigil.protomongo.service.NoSqlService;

@RestController
@RequestMapping(path = "/mongo/1")
public class MongoController {

	private final NoSqlService mongoService;

	public MongoController(@Autowired final NoSqlService mongoService) {
		this.mongoService = mongoService;
	}

	@GetMapping(path = "/databases")
	public ResponseEntity<List<Document>> listDatabaseNames() {
		return ResponseEntity.ok(mongoService.getDatabaseNames());
	}

}
