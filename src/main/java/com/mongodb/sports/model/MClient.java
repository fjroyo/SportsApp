package com.mongodb.sports.model;

import com.mongodb.client.*;
import org.bson.Document;


public class MClient {
    private static final String CONNECTION_STRING = "";
    // ##YOUR_TASK##:  FILL YOUR CONNECTION STRING HERE

    private static final String DATABASE = "sports";
    private static final String USERS = "users";
    private static final String EVENTS = "events";

    private static MongoClient mongoClient;

    static {
        mongoClient = MongoClients.create(CONNECTION_STRING);
    }

    public MongoClient getClient() {
        return mongoClient;
    }

    public MongoCollection<Document> getUsersCollection() {
        return mongoClient.getDatabase(DATABASE).getCollection(USERS);
    }

    public MongoCollection<Document> getEventsCollection() {
        return mongoClient.getDatabase(DATABASE).getCollection(EVENTS);
    }
}

