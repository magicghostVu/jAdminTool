package controller;

import domain.User;
import globalAppContext.GlobalApplicationContextWrap;
import model.UserModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import restful.response.RestfulErrorDefine;
import services.AuthenticationService;
import services.AuthenticationServiceImpl;
import services.UserManagementService;
import services.UserManagementServiceImpl;
import utils.JsonUtis;

/**
 * Created by Fresher on 30/06/2017.
 */
@EnableAutoConfiguration
@RestController
public class InfoController {


    private AuthenticationService authenticationService = GlobalApplicationContextWrap.getInstance()
            .getApplicationContext().getBean("authentication_service", AuthenticationServiceImpl.class);

    UserManagementService userManagementService= GlobalApplicationContextWrap.getInstance()
            .getApplicationContext().getBean("user_management_service", UserManagementServiceImpl.class);

    @RequestMapping(value = "/get-info", method = RequestMethod.POST, consumes = "application/json")
    public String getInfo(@RequestBody String jsonData) {
        try {
            JSONObject jsonParam = new JSONObject(jsonData);
            int uid = jsonParam.getInt("uid");
            String accToken = jsonParam.getString("acc_token");
            UserModel userModel = authenticationService.authenAccessToken(accToken);

            // accessToken is not valid
            if (userModel == null) {
                return JsonUtis.createResponseString(RestfulErrorDefine.ACCESS_TOKEN_INVALID, "");
            } else {
                User user = userManagementService.getUserByUsername(userModel.getUsername());
                
                if(user==null){

                }else {

                }


            }

            //System.out.println(service==null);

        } catch (JSONException je) {
            return JsonUtis.createResponseString(RestfulErrorDefine.REQUEST_BODY_INVALID, "");
        }
        return null;
    }


}





