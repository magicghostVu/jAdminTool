package controller;

import config.ServerConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import restful.response.BaseRestResponse;
import restful.response.RestfulErrorDefine;

/**
 * Created by Fresher on 19/06/2017.
 */

@RestController
@EnableAutoConfiguration
public class AccountController {
    @RequestMapping(value = "/home")
    public String home(){
        return "home";
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public String login(@RequestBody String jsonData){
        try {
            JSONObject dataLogin= new JSONObject(jsonData);
            // implement authentication service here

            return null;

        }catch (JSONException jo){
            //JSONObject jsonLoginData= new JSONObject(jsondata);
            BaseRestResponse restResponse= new BaseRestResponse(
                    RestfulErrorDefine.REQUEST_BODY_INVALID, "");
            return ServerConfig.globalGson.toJson(restResponse);
        }
    }



}
