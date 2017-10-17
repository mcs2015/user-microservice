package com.stl.user.database;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    /*@Value("#{('${MONGO_DBNAME}' !== '' && '${MONGO_DBNAME}' !== null )? " +
            "'${MONGO_DBNAME}' : 'haulmatic'}")*/
    @Value("${spring.data.mongodb.database}")
    private String dbName;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        MongoCredential mongoCredential =
                MongoCredential.createCredential(username, dbName, password.toCharArray());

        return new MongoClient(new ServerAddress(host, port), Arrays.asList(mongoCredential));
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        return new MongoTemplate(mongo(), dbName);
    }
}
