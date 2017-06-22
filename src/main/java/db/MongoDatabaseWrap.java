package db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.ServerConfig;

import java.util.HashSet;

/**
 * Created by magic_000 on 20/06/2017.
 */
public class MongoDatabaseWrap {
    private MongoDatabase mongoDatabase;
    public MongoDatabaseWrap() {
        MongoClient mongoClient = new MongoClient(ServerConfig.HOST_MONGO, ServerConfig.PORT_MONGO);
        this.mongoDatabase = mongoClient.getDatabase(ServerConfig.DB_NAME);
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }


    public boolean collectionNameExist(String collectionName) {
        return mongoDatabase.listCollectionNames().into(new HashSet<>()).contains(collectionName);
    }

    public MongoCollection getMongoCollection(String collectionName) {
        if (collectionNameExist(collectionName)) {
            return this.mongoDatabase.getCollection(collectionName);
        } else {
            return null;
        }
    }
}
