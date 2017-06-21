package services;

import utils.MyPair;

/**
 * Created by Fresher on 21/06/2017.
 */
public interface AuthenticationService {
    MyPair<String ,String> authenUsernamePassword(String username, String password);
    String authen
}
