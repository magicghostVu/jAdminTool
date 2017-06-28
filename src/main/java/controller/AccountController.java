package controller;

import config.ServerConfig;
import domain.User;
import globalAppContext.GlobalApplicationContextWrap;
import model.UserDAO;
import model.UserDAOImpl;
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

    private AuthenticationService authenticationService = GlobalApplicationContextWrap.getInstance()
            .getApplicationContext().getBean("authentication_service", AuthenticationServiceImpl.class);

    private UserManagementService userManagementService = GlobalApplicationContextWrap.getInstance()
            .getApplicationContext().getBean("user_management_service", UserManagementServiceImpl.class);

    private UserDAO userDAO= GlobalApplicationContextWrap.getInstance()
            .getApplicationContext().getBean("user_DAO_impl", UserDAOImpl.class);

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
                // if it had existed update it and continue use it
                // if it is not exist, create new user with data and put it in to the maps
                // (update it into userManagement service )
                UserModel userModel= userDAO.getUserModelByUserName(pairAuthen.get_1());
                //get user by user name from Map
                User user = userManagementService.getUserByUsername(pairAuthen.get_1());
                if (user == null) {
                    System.out.println("new user login and put it to map");
                    user= new User(userModel);
                    user.handShakeAndLogin();
                    userManagementService.addUserToMap(user);
                } else {
                    user.setModel(userModel);
                    user.updateInteractTime();
                }
                return createResponseString(RestfulErrorDefine.SUCCESS, pairAuthen.get_2());
            } else {
                return createResponseString(RestfulErrorDefine.USERNAME_OR_PASSWORD_INVALID, "");
            }
        } catch (JSONException jo) {
            return createResponseString(RestfulErrorDefine.REQUEST_BODY_INVALID, "");
        }
    }


    // refactor
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

    //rest api for super admin
    public String addAccount(String jsonData) {


        return null;
    }

    private String createResponseString(RestfulErrorDefine err, String data) {
        BaseRestResponse restResponse = new BaseRestResponse(err, data);
        return ServerConfig.globalGson.toJson(restResponse);
    }
}
