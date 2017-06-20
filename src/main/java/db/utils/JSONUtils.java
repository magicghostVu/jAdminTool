package db.utils;

import model.AbstractModel;
import org.bson.Document;
import org.json.JSONObject;

/**
 * Created by Fresher on 20/06/2017.
 */
public class JSONUtils {
    public static Document convertModelToDocument(AbstractModel model){
        String jsonString = model.toJsonString();
        JSONObject jo= new JSONObject(jsonString);
        Document res= new Document();
        jo.keySet().forEach(key-> res.append(key, jo.get(key)));
        return res;
    }
}
