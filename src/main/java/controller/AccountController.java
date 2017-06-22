package controller;

import config.ServerConfig;
import globalAppContext.GlobalApplicationContextWrap;
import model.UserModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import restful.response.BaseRestResponse;
import restful.response.RestfulErrorDefine;
import services.AuthenticationService;
import services.AuthenticationServiceImpl;
import utils.MyPair;

/**
 * Created by Fresher on 19/06/2017.
 */

@RestController
@EnableAutoConfiguration
public class AccountController {

    AuthenticationService authenticationService = GlobalApplicationContextWrap.getInstance()
            .getApplicationContext().getBean("authentication_service", AuthenticationServiceImpl.class);

    @RequestMapping(value = "/home")
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public String login(@RequestBody String jsonData) {
        try {
            JSONObject dataLogin = new JSONObject(jsonData);
            // implement authentication service here
            String username = dataLogin.getString("username");
            String password = dataLogin.getString("password");
            /*AuthenticationService authenticationService = GlobalApplicationContextWrap.getInstance()
                    .getApplicationContext().getBean("authentication_service", AuthenticationServiceImpl.class);*/
            MyPair<String, String> pairAuthen = authenticationService.authenUsernamePassword(username, password);
            if (pairAuthen != null) {
                return createResponseString(RestfulErrorDefine.SUCCESS, pairAuthen.get_2());
            } else {
                return createResponseString(RestfulErrorDefine.USERNAME_OR_PASSWORD_INVALID, "");
            }
        } catch (JSONException jo) {
            return createResponseString(RestfulErrorDefine.REQUEST_BODY_INVALID, "");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = "application/json")
    public String logout(@RequestBody String data) {
        try {
            JSONObject dataAccTk = new JSONObject(data);
            String accessToken = dataAccTk.getString("access-token");
            /*AuthenticationService authenticationService = GlobalApplicationContextWrap.getInstance()
                    .getApplicationContext().getBean("authentication_service", AuthenticationServiceImpl.class);*/
            UserModel model = authenticationService.authenAccessToken(accessToken);
            if (model != null) {
                model.clearAccessToken();
                return createResponseString(RestfulErrorDefine.SUCCESS, model.getUsername());
            } else {
                return createResponseString(RestfulErrorDefine.FAILED, "");
            }

        } catch (JSONException jo) {
            return createResponseString(RestfulErrorDefine.REQUEST_BODY_INVALID, "");
        }
        //return null;
    }

    @RequestMapping(value = "authen-acctk", method = RequestMethod.POST, consumes = "application/json")
    public String testAuthenAccToken(@RequestBody String jsonData) {
        try {
            JSONObject dataObject = new JSONObject(jsonData);
            String accessToken = dataObject.getString("access-token");
            /*AuthenticationService authenticationService = GlobalApplicationContextWrap.getInstance()
                    .getApplicationContext().getBean("authentication_service", AuthenticationServiceImpl.class);*/
            UserModel userModel = authenticationService.authenAccessToken(accessToken);
            if (userModel != null) {
                return createResponseString(RestfulErrorDefine.SUCCESS, userModel.getUsername());
            } else {
                return createResponseString(RestfulErrorDefine.FAILED, "");
            }
            //return null;
        } catch (JSONException jo) {
            return createResponseString(RestfulErrorDefine.REQUEST_BODY_INVALID, "");
        }
    }

    private String createResponseString(RestfulErrorDefine err, String data) {
        BaseRestResponse restResponse = new BaseRestResponse(err, data);
        return ServerConfig.globalGson.toJson(restResponse);
    }
}
