package model;

import com.mongodb.client.MongoCollection;
import config.ServerConfig;
import config.constant.CollectionName;
import db.MongoDatabaseWrap;
import org.bson.Document;
import org.json.JSONObject;

/**
 * Created by Fresher on 20/06/2017.
 */
public class UserDAOImpl implements UserDAO {

    private MongoDatabaseWrap wrap;
    private CollectionName collectionName = CollectionName.USER;
    private MongoCollection userCollection;

    public UserDAOImpl() {

    }

    @Override
    public UserModel getUserModelByUserName(String username) {
        //todo: get data from DB, convert to model and return
        MongoCollection userCollection = getUserCollection();
        Document query = new Document();
        query.put("_id", username);
        Document document = (Document) userCollection.find(query).limit(1).first();
        if (document != null) {
            return convertDocumentToUserModel(document);
        } else {
            //return null if can not find any model with above username
            return null;
        }
    }



    private UserModel convertDocumentToUserModel(Document userDocument){
        JSONObject tmpJo = new JSONObject();
        userDocument.keySet().forEach(key -> {
            tmpJo.put(key, userDocument.get(key));
        });
        tmpJo.put("username", tmpJo.get("_id"));
        tmpJo.remove("_id");
        return ServerConfig.globalGson.fromJson(tmpJo.toString(), UserModel.class);
    }



    @Override
    public boolean saveUserModel(UserModel model) {
        try {
            MongoCollection userCollection = getUserCollection();
            if (modelExist(model)) {
                Document queryId= new Document();
                queryId.put("_id", model.getUsername());
                userCollection.updateOne(queryId, new Document("$set",model.toDocument()));
                return true;
            } else {
                userCollection.insertOne(model.toDocument());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modelExist(UserModel model) {
        Document query = new Document();
        query.put("_id", model.getUsername());
        MongoCollection userCollection = getUserCollection();
        return userCollection.find(query).limit(1).first() != null;
    }

    @Override
    public UserModel getModelByAccessToken(String accTk) {
        try{
            MongoCollection userCollection= getUserCollection();
            Document queryByAccTk= new Document();
            queryByAccTk.put("accessToken", accTk);
            Document resDocument= (Document)userCollection.find(queryByAccTk).limit(1).first();
            if(resDocument!=null){
                return convertDocumentToUserModel(resDocument);
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public MongoDatabaseWrap getWrap() {
        return wrap;
    }

    public void setWrap(MongoDatabaseWrap wrap) {
        this.wrap = wrap;
        setUserCollection(wrap.getMongoCollection(collectionName.getColletionName()));
    }

    public MongoCollection getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(MongoCollection userCollection) {
        this.userCollection = userCollection;
    }
}
