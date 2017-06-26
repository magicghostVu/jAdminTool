package controller;

import config.ServerConfig;
import domain.User;
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
import services.UserManagementService;
import services.UserManagementServiceImpl;
import utils.MyPair;

/**
 * Created by Fresher on 19/06/2017.
 */

@RestController
@EnableAutoConfiguration
public class AccountController {

    AuthenticationService authenticationService = GlobalApplicationContextWrap.getInstance()
            .getApplicationContext().getBean("authentication_service", AuthenticationServiceImpl.class);

    UserManagementService userManagementService = GlobalApplicationContextWrap.getInstance()
            .getApplicationContext().getBean("user_management_service", UserManagementServiceImpl.class);


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
            MyPair<String, String> pairAuthen = authenticationService.authenUsernamePassword(username, password);
            if (pairAuthen != null) {


                //todo: check if user exist in UserManagement,
                // if had update it and continue use it
                // if it is not exist, create new user with data and put it in to the maps
                // (update it into userManagement service )

                // get user by user name from Map
                User user = userManagementService.getUserByUsername(pairAuthen.get_1());
                if (user == null) {
                    // user first login so, create new user and put it to userManagement service

                    

                } else {
                    // update it
                }


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
            UserModel model = authenticationService.authenAccessToken(accessToken);
            if (model != null) {
                model.clearAccessToken();
                return createResponseString(RestfulErrorDefine.SUCCESS, "logout: " + model.getUsername());
            } else {
                return createResponseString(RestfulErrorDefine.FAILED, "");
            }

        } catch (JSONException jo) {
            return createResponseString(RestfulErrorDefine.REQUEST_BODY_INVALID, "");
        }
        //return null;
    }

    @RequestMapping(value = "test-authen-acctk", method = RequestMethod.POST, consumes = "application/json")
    public String testAuthenAccToken(@RequestBody String jsonData) {
        try {
            JSONObject dataObject = new JSONObject(jsonData);
            String accessToken = dataObject.getString("access-token");
            UserModel userModel = authenticationService.authenAccessToken(accessToken);
            if (userModel != null) {
                return createResponseString(RestfulErrorDefine.SUCCESS, userModel.getUsername());
            } else {
                return createResponseString(RestfulErrorDefine.FAILED, "");
            }
        } catch (JSONException jo) {
            return createResponseString(RestfulErrorDefine.REQUEST_BODY_INVALID, "");
        }
    }

    public String addAccount(String jsonData) {


        return null;
    }

    private String createResponseString(RestfulErrorDefine err, String data) {
        BaseRestResponse restResponse = new BaseRestResponse(err, data);
        return ServerConfig.globalGson.toJson(restResponse);
    }
}
