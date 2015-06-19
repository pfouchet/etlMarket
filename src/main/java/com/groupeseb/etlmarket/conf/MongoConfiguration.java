package com.groupeseb.etlmarket.conf;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;

@Configuration
public class MongoConfiguration {

	@Value("${mongo.db:datastore}")
	private String databaseName;

	@Value("${mongo.host:localhost}")
	private String databaseHost;

	@Value("${mongo.port: 27017}")
	private Integer databasePort;

	@Bean
	public MongoTemplate mongoTemplate() throws UnknownHostException {
		return new MongoTemplate(customMongoDbFactory());
	}

	@Bean
	public MongoDbFactory customMongoDbFactory() throws UnknownHostException {
		return new SimpleMongoDbFactory(mongo(), databaseName);
	}

	@Bean
	public Mongo mongo() throws UnknownHostException {
		return new Mongo(databaseHost, databasePort);
	}
}
