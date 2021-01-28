package com.zirakzigil.protomongo.service;

import java.util.List;

import org.bson.Document;

public interface NoSqlService {

	public List<Document> getDatabaseNames();

}
