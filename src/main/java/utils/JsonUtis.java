package utils;

import config.ServerConfig;
import restful.response.BaseRestResponse;
import restful.response.RestfulErrorDefine;

/**
 * Created by Fresher on 30/06/2017.
 */
public class JsonUtis {
    public static String createResponseString(RestfulErrorDefine err, String data) {
        BaseRestResponse restResponse = new BaseRestResponse(err, data);
        return ServerConfig.globalGson.toJson(restResponse);
    }
}
