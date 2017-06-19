package db;

import com.mongodb.MongoClient;

/**
 * Created by magic_000 on 20/06/2017.
 */
public class MongoClientWrap {
    public MongoClient mongoClient;

    public MongoClientWrap(){
        this.mongoClient= new MongoClient();
    }

}
