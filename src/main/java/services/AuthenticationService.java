package services;

import model.UserModel;
import utils.MyPair;

/**
 * Created by Fresher on 21/06/2017.
 */
public interface AuthenticationService {

    //return pair contain username and accessToken if authenticate success
    MyPair<String ,String> authenUsernamePassword(String username, String password);
    // return user name if accessToken is valid
    UserModel authenAccessToken(String accessToken);

}
